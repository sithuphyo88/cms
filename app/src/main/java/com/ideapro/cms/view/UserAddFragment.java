package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.RoleEntity;
import com.ideapro.cms.data.UserEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.RoleSpinnerAdapter;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserAddFragment extends Fragment {

    private static String BARG_USER_ID;
    View view;
    Menu menu;

    @BindView(R.id.spnRole)
    Spinner spnRole;

    @BindView(R.id.txtUserName)
    TextView tvUserName;

    @BindView(R.id.txtPassword)
    TextView tvPassword;

    @BindView(R.id.tvwRole)
    TextView tvRole;

    @BindView(R.id.txtPhone)
    TextView tvPhone;

    @BindView(R.id.txtEmail)
    TextView tvEmail;

    DaoFactory daoFactory;
    UserEntity user;
    private boolean flag_update;
    List<Boolean> selectedList;
    List<RoleEntity> roleList;


    public static UserAddFragment newInstance(String userId) {

        Bundle args = new Bundle();
        args.putString(BARG_USER_ID, userId);
        UserAddFragment fragment = new UserAddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public UserAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_add, container, false);
        setHasOptionsMenu(true);

        ButterKnife.bind(this, view);
        daoFactory = new DaoFactory(view.getContext());

        initializeUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_user));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        Bundle bundle = getArguments();
        user = new UserEntity();

        String userId = "";
        if (bundle != null) {
            userId = bundle.getString(BARG_USER_ID);
        }

        try {
            Dao<UserEntity, String> userDao = daoFactory.getUserEntityDao();
            user = userDao.queryForId(userId);

            selectedList = new ArrayList<Boolean>();

            Dao<RoleEntity, String> roleDao = daoFactory.getRoleEntityDao();
            roleList = roleDao.queryForAll();

            // for role list
            String[] roles = new String[roleList.size() + 1];
            String[] selectedUsers;

            for (int i = 0; i < roleList.size(); i++) {
                roles[i] = roleList.get(i).name;
            }
            roles[roleList.size()] = getString(R.string.choose_role);

            if (user == null) {
                selectedUsers = new String[]{};
            } else {
                selectedUsers = user.role.split(",");
                if (selectedUsers.length == 0) {
                    selectedUsers[0] = user.role;
                }
            }

            for (int i = 0; i < roles.length - 1; i++) {
                selectedList.add(false);
                String roleId = roleList.get(i).id;
                for (int j = 0; j < selectedUsers.length; j++) {
                    String selectedUserRoleId = selectedUsers[j];
                    if (selectedUserRoleId.equals(roleId)) {
                        selectedList.set(i, true);
                        break;
                    }
                }
            }

            ArrayAdapter<String> mAdapter = new RoleSpinnerAdapter(this, view.getContext(), getActivity(), roles, selectedList);
            spnRole.setAdapter(mAdapter);

            spnRole.setSelection(-1);
           /* if (userId.isEmpty()) {
                spnRole.setSelection(mAdapter.getCount());
            } else {
                int index = -1;
                for (int i = 0; i < roleList.size(); i++) {
                    if (roleList.get(i).id.equals(user.role)) {
                        index = i;
                        break;
                    }
                }
                spnRole.setSelection(index);
            }*/


            if (userId.isEmpty()) {
                reset();
            } else {
                flag_update = true;
                // set data to the view
                tvUserName.setText(user.name);
                tvEmail.setText(user.email);
                tvPhone.setText(user.phone);
                tvPassword.setText(user.password);
                tvRole.setText(user.role);

            }
        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    private void reset() {
        tvUserName.setText("");
        tvEmail.setText("");
        tvPhone.setText("");
        tvPassword.setText("");
        tvRole.setText("");
        user = new UserEntity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (validation()) {
            try {
                getData();
                if (flag_update) {
                    updateData();
                } else {
                    saveData();
                }
                reset();
            } catch (Exception e) {
                throw new Error(e);
            }

            if (!flag_update) {
                Toast.makeText(getContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private void saveData() throws SQLException {
        Dao<UserEntity, String> userDao = daoFactory.getUserEntityDao();
        user.id = UUID.randomUUID().toString();
        userDao.create(user);
    }

    private void updateData() throws SQLException {
        Dao<UserEntity, String> userDao = daoFactory.getUserEntityDao();
        userDao.update(user);
    }

    private void getData() {
        user.email = tvEmail.getText().toString();
        user.name = tvUserName.getText().toString();
        user.password = tvPassword.getText().toString();
        user.phone = tvPhone.getText().toString();


        /*user.role = tvRole.getText().toString();*/
        user.role = getRoles();
    }

    private String getRoles() {
        String selectedRole = "";
        String splitter = ",";
        boolean first = true;
        String[] roles = new String[roleList.size()];

        for (int i = 0; i < roleList.size(); i++) {
            roles[i] = roleList.get(i).id;
        }

        for (int i = 0; i < roles.length; i++) {
            if (selectedList.get(i) == true) {
                if (first) {
                    selectedRole += roles[i];
                } else {
                    selectedRole += splitter + roles[i];
                }
                first = false;
            }
        }
        return selectedRole;
    }

    public void selectCheckBox(View v) {
        CheckBox cb = (CheckBox) v;
        selectedList.set(cb.getId(), cb.isChecked());
    }

    // START Added 2016/09/09 Sai Num Town
    private boolean validation() {
        boolean flag = true;
        // Check user name
        if (TextUtils.isEmpty(tvUserName.getText().toString())) {
            tvUserName.setError(getString(R.string.error_missing_user_name));
            flag = false;
        }

        // check phone number
        if (!TextUtils.isEmpty(tvPhone.getText().toString())) {
            if (!CommonUtils.isValidPhoneNumber(tvPhone.getText().toString())) {
                tvPhone.setError(getString(R.string.error_wrong_format_phone_number));
                flag = false;
            }
        }

        // check email
        if (!TextUtils.isEmpty(tvEmail.getText().toString())) {
            if (!CommonUtils.isEmailValid(tvEmail.getText().toString())) {
                tvEmail.setError(getString(R.string.error_wrong_email_format));
                flag = false;
            }
        }

        // check password
        if (TextUtils.isEmpty(tvPassword.getText().toString())) {
            tvPassword.setError(getString(R.string.error_missing_password));
            flag = false;
        }
        return flag;
    }
    // END Added 2016/09/09 Sai Num Town
}
