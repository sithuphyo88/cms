package com.ideapro.cms.view;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.UserEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.utils.cmsAppConstants;
import com.ideapro.cms.view.listAdapter.EngineerListAdapter;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EngineerListFragment extends Fragment {

    private static final String QUERY_SELECT_ENGINEER_ASSIGN = "SELECT engineer_id\n" +
            "FROM projects_engineers";
    private static final String ROLE_ENGINEER = "5";
    private static final String QUERY_INSERT_ENGINEER_ASSIGN = "INSERT INTO `projects_engineers`(`project_id`,`engineer_id`) VALUES ";
    private static final String QUERY_DELETE_ENGINEER_ASSIGN = "DELETE FROM `projects_engineers` WHERE `project_id`= ";
    View view;
    ProjectEntity projectEntity;
    List<UserEntity> list;
    List<Boolean> selectedList;
    EngineerListAdapter adapter;
    ImageButton imgAdd;
    Menu menu;
    DaoFactory daoFactory;

    public EngineerListFragment() {
    }

    public EngineerListFragment(ProjectEntity projectEntity) {
        if (projectEntity == null) {
            this.projectEntity = new ProjectEntity();
        } else {
            this.projectEntity = projectEntity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_engineer_list, container, false);
        setHasOptionsMenu(true);
        daoFactory = new DaoFactory(view.getContext());

        bindData();
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_engineer_assign) + " for " + this.projectEntity.name);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add data to the database

                try {
                    Dao<UserEntity, String> userDao = daoFactory.getUserEntityDao();
                    String whereParameter = projectEntity.id.toString().trim();
                    GenericRawResults<String[]> rawResults = userDao.queryRaw(QUERY_DELETE_ENGINEER_ASSIGN + whereParameter);
                    for (int i = 0; i < list.size(); i++) {
                        if (selectedList.get(i) == true) {
                            UserEntity userEntity = list.get(i);
                            String insertParamenter =" (" + projectEntity.id + "," + userEntity.id + ")";
                            userDao.queryRaw(QUERY_INSERT_ENGINEER_ASSIGN + insertParamenter);
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }


                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), ProjectAddFragment.newInstance(projectEntity.id));
            }
        });

        imgAdd.setVisibility(View.GONE);
    }

    private void bindData() {
        try {
            list = new ArrayList<>();
            List<UserEntity> selectedEngineerList = new ArrayList<>();
            selectedList = new ArrayList<Boolean>();


           /* for (int i = 0; i < 30; i++) {
                UserEntity entity = new UserEntity();
                entity.name = "Engineer " + (i + 1);
                list.add(entity);
                selectedList.add(false);
            }*/

            Dao<UserEntity, String> userDao = daoFactory.getUserEntityDao();

            String whereParameter = " WHERE project_id=" + projectEntity.id.toString().trim();

            GenericRawResults<String[]> rawResults = userDao.queryRaw(QUERY_SELECT_ENGINEER_ASSIGN + whereParameter);

            selectedEngineerList = ConvertToObject(rawResults);
            rawResults.close();
            // page through the results

            list = userDao.queryForEq(UserEntity.ROLE_ID, cmsAppConstants.ROLE_ENGINEER);


            for (int i = 0; i < list.size(); i++) {
                selectedList.add(false);
                UserEntity engineer = list.get(i);
                for (int j = 0; j < selectedEngineerList.size(); j++) {
                    UserEntity selectedEngineer = selectedEngineerList.get(j);
                    if (selectedEngineer.id.equals(engineer.id)) {
                        selectedList.set(i, true);
                        break;
                    }
                }
            }

            adapter = new EngineerListAdapter(this, view.getContext(), getActivity(), list, selectedList);
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
                    CheckBox chkEngineerName = (CheckBox) innerView.findViewById(position);
                    chkEngineerName.setChecked(!chkEngineerName.isChecked());
                }
            });

        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private List<UserEntity> ConvertToObject(GenericRawResults<String[]> rawResults) {
        List<UserEntity> userEntities = new ArrayList<>();
        for (String[] resultArray : rawResults) {
            UserEntity userEntity = new UserEntity();
            userEntity.id = resultArray[UserEntity.COLUMN_ID];
            userEntities.add(userEntity);
        }
        return userEntities;
    }

    public void selectCheckBox(View v) {
        CheckBox cb = (CheckBox) v;
        selectedList.set(cb.getId(), cb.isChecked());

        if (hasSelectedEngineer()) {
            imgAdd.setVisibility(View.VISIBLE);
        } else {
            imgAdd.setVisibility(View.GONE);
        }
    }

    private boolean hasSelectedEngineer() {
        for (int i = 0; i < this.selectedList.size(); i++) {
            if (this.selectedList.get(i)) {
                return true;
            }
        }

        return false;
    }
}
