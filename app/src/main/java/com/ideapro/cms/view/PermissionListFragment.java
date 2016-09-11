package com.ideapro.cms.view;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.PermissionEntity;
import com.ideapro.cms.data.RoleEntity;
import com.ideapro.cms.view.listAdapter.PermissionListAdapter;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermissionListFragment extends Fragment {

    private static final String QUERY_SELECT_ROLE_PERMISSION = "SELECT rp.permission_id \n" +
            ", r.name\n" +
            ", r.id as role_id  \n" +
            "FROM role r \n" +
            "INNER JOIN roles_permissions rp \n" +
            "ON r.id = rp.role_id \n" +
            "WHERE r.id =";
    private static final String QUERY_DELETE_PERMISSION_SELECTED = "DELETE FROM `roles_permissions` WHERE `role_id`= ";
    private static final String QUERY_INSERT_PERMISSION_SELECTED = "INSERT INTO `roles_permissions`(`role_id`,`permission_id`) VALUES ";

    private static final String BARG_ROLE_ID = "role_id";
    View view;
    List<PermissionEntity> list;
    PermissionListAdapter adapter;
    Menu menu;
    private DaoFactory daoFactory;
    List<Boolean> selectedList;

    public PermissionListFragment() {
    }

    public static PermissionListFragment newInstance(String roleId) {

        Bundle args = new Bundle();
        args.putString(BARG_ROLE_ID, roleId);

        PermissionListFragment fragment = new PermissionListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_permission_list, container, false);
        setHasOptionsMenu(true);
        //
        daoFactory = new DaoFactory(view.getContext());
        bindData();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_assign_permission));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void bindData() {
        try {
            selectedList = new ArrayList<Boolean>();
            List<PermissionEntity> selectedPermissions = new ArrayList<>();
            // dummy data.
           /* list = new ArrayList<>();

            for (int i = 0; i < 30; i++) {
                PermissionEntity entity = new PermissionEntity();
                entity.title = "Permission " + (i + 1);
                entity.description = "XXXXXXXXXXXXXXXXXXXXXXXX";
                list.add(entity);
            }*/
            Dao<PermissionEntity, String> permissionEntityDao = daoFactory.getPermissionEntityDao();
           list = permissionEntityDao.queryForAll();

            String whereParameter = getRoleId().toString().trim();
            GenericRawResults<String[]> rawResults = permissionEntityDao.queryRaw(QUERY_SELECT_ROLE_PERMISSION + whereParameter);

            selectedPermissions = ConvertToObject(rawResults);
            rawResults.close();

            for (int i = 0; i < list.size(); i++) {
                selectedList.add(false);
                PermissionEntity per = list.get(i);
                for (int j = 0; j < selectedPermissions.size(); j++) {
                    PermissionEntity selectedPer = selectedPermissions.get(j);
                    if (selectedPer.id.equals(per.id)) {
                        selectedList.set(i, true);
                        break;
                    }
                }
            }

            adapter = new PermissionListAdapter(this, view.getContext(), getActivity(), list, selectedList);
            ListView listView = (ListView) view.findViewById(R.id.listView);
            ColorDrawable myColor = new ColorDrawable(
                    this.getResources().getColor(R.color.color_accent)
            );
            listView.setDivider(myColor);
            listView.setDividerHeight(1);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View innerView, int position, long id) {
                    CheckBox chk = (CheckBox) innerView.findViewById(position);
                    chk.setChecked(!chk.isChecked());
                }
            });

        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private List<PermissionEntity> ConvertToObject(GenericRawResults<String[]> rawResults) {

        List<PermissionEntity> permissionEntities = new ArrayList<>();

        for (String[] resultArray : rawResults) {
            PermissionEntity permissionEntity = new PermissionEntity();
            permissionEntity.id = resultArray[PermissionEntity.COLUMN_ID];
            permissionEntities.add(permissionEntity);
        }

        return permissionEntities;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (hasSelectedItem()) {
            Dao<PermissionEntity, String> perDao = null;
            try {
                perDao = daoFactory.getPermissionEntityDao();
                String whereParameter = getRoleId();
                GenericRawResults<String[]> rawResults = perDao.queryRaw(QUERY_DELETE_PERMISSION_SELECTED + whereParameter);

                for (int i = 0; i < list.size(); i++) {
                    if (selectedList.get(i) == true) {
                        PermissionEntity perEntity = list.get(i);
                        String insertParamenter = " (" + getRoleId() + "," + perEntity.id + ")";
                        perDao.queryRaw(QUERY_INSERT_PERMISSION_SELECTED + insertParamenter);
                    }

                }
                Toast.makeText(getContext(), getString(R.string.info_register_succesfully), Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                throw new Error(e);
            }
        } else {
            Toast.makeText(getContext(), getString(R.string.error_missing_check), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private boolean validation() {
        boolean flag = true;

        return flag;
    }

    private String getRoleId() {
        Bundle bundle = getArguments();
        String siteId = "";
        if (bundle != null) {
            siteId = bundle.getString(BARG_ROLE_ID);
        }
        return siteId;
    }

    public void selectCheckBox(View v) {
        CheckBox cb = (CheckBox) v;
        selectedList.set(cb.getId(), cb.isChecked());

    }

    private boolean hasSelectedItem() {
        for (int i = 0; i < this.selectedList.size(); i++) {
            if (this.selectedList.get(i)) {
                return true;
            }
        }
        return false;
    }
}
