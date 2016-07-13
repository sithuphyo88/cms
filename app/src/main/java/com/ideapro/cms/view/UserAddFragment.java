package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.UserEntity;
import com.ideapro.cms.view.listAdapter.RoleSpinnerAdapter;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
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
        spnRole = (Spinner) view.findViewById(R.id.spnRole);
        String[] customers = new String[]{"Manager", "Designer", "Buyer", "Engineer"};

        ArrayAdapter<String> mAdapter = new RoleSpinnerAdapter(view.getContext(), getActivity(), customers);
        spnRole.setAdapter(mAdapter);

        Bundle bundle = getArguments();
        user = new UserEntity();
        String userId = "";
        if (bundle != null) {
            userId = bundle.getString(BARG_USER_ID);

        }
        if (userId.isEmpty()) {
            reset();
        } else {

            try {
                // get the data form the table
                flag_update = true;
                Dao<UserEntity, String> userDao = daoFactory.getUserEntityDao();
                user = userDao.queryForId(userId);

                // set data to the view
                tvUserName.setText(user.name);
                tvEmail.setText(user.email);
                tvPhone.setText(user.phone);
                tvPassword.setText(user.password);
                tvRole.setText(user.role);

            } catch (SQLException e) {
                throw new Error(e);
            }
        }
    }

    private void reset() {
        tvUserName.setText("");
        tvEmail.setText("");
        tvPhone.setText("");
        tvPassword.setText("");
        tvRole.setText("Manager");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        user.role = tvRole.getText().toString();
    }
}
