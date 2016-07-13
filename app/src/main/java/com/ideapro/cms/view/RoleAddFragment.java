package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.RoleEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoleAddFragment extends Fragment {

    private static String BARG_ROLE_ID;
    View view;
    Menu menu;

    @BindView(R.id.txtRoleName)
    TextView tvRoleName;

    @BindView(R.id.txtDescription)
    TextView tvDescription;

    DaoFactory daoFactory;

    RoleEntity role;
    private boolean flag_update;

    public static RoleAddFragment newInstance(String roleId) {

        Bundle args = new Bundle();
        args.putString(BARG_ROLE_ID, roleId);
        RoleAddFragment fragment = new RoleAddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RoleAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_role_add, container, false);
        setHasOptionsMenu(true);

        ButterKnife.bind(this, view);
        daoFactory = new DaoFactory(view.getContext());

        initializeUI();

        Button butAssignPermission = (Button) view.findViewById(R.id.butAssignPermission);
        butAssignPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PermissionListFragment());
            }
        });

        return view;
    }

    private void initializeUI() {
        Bundle bundle = getArguments();
        role = new RoleEntity();
        String roleId = "";

        if (bundle != null) {
            roleId = bundle.getString(BARG_ROLE_ID);
        }

        if (roleId.isEmpty()) {
            reset();
        } else {
            try {
                // get the data form the table
                flag_update = true;
                Dao<RoleEntity, String> roleDao = daoFactory.getRoleEntityDao();
                role = roleDao.queryForId(roleId);
                tvDescription.setText(role.description);
                tvRoleName.setText(role.name);

            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    private void reset() {
        tvDescription.setText("");
        tvRoleName.setText("");
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
        Dao<RoleEntity, String> roleDao = daoFactory.getRoleEntityDao();
        role.id = UUID.randomUUID().toString();
        roleDao.create(role);
    }

    private void updateData() throws SQLException {
        Dao<RoleEntity, String> roleDao = daoFactory.getRoleEntityDao();
        roleDao.update(role);
    }

    // get data from the textbox
    private void getData() {
        role.name = tvRoleName.getText().toString();
        role.description = tvDescription.getText().toString();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_role));
        super.onCreateOptionsMenu(menu, inflater);
    }
}
