package com.ideapro.cms.view;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SubContractorEntity;
import com.ideapro.cms.data.UserEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.SubContractorSelectBoxListAdapter;
import com.ideapro.cms.view.swipeMenu.SwipeMenuListView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubContractorSelectedBoxListFragment extends Fragment {
    private static final String QUERY_SELECT_SUB_CONTRACTOR_ASSIGN = "SELECT subContracotr_id\n" +
            "\t\t\t, name\n" +
            "\t\t\t, phone\n" +
            "\t\t\t, email\n" +
            "\t\t\t, address\n" +
            "FROM subContractor s\n" +
            "INNER JOIN projects_sub_contractors ps\n" +
            "ON ps.sub_contractor_id = s.subContracotr_id \n" +
            "WHERE ps.project_id= ";
    private static final String QUERY_DELETE_SUB_CONTRACTOR_ASSIGN = "DELETE FROM `projects_sub_contractors` WHERE `project_id`= ";
    private static final String QUERY_INSERT_SUB_CONTRACTOR_ASSIGN = "INSERT INTO `projects_sub_contractors`(`project_id`,`sub_contractor_id`) VALUES ";
    View view;
    List<SubContractorEntity> list;
    SubContractorSelectBoxListAdapter adapter;
    List<Boolean> selectedList;
    Menu menu;
    ImageButton imgAdd;
    ProjectEntity projectEntity;
    private DaoFactory daoFactory;

    public SubContractorSelectedBoxListFragment() {
        // Required empty public constructor
    }

    public SubContractorSelectedBoxListFragment(ProjectEntity projectEntity) {
        // Required empty public constructor
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
        view = inflater.inflate(R.layout.fragment_sub_contractor_select_box_list, container, false);
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
        getActivity().setTitle(getString(R.string.label_choose_sub_contractor));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);
        imgAdd.setVisibility(View.GONE);

        if (CommonUtils.CurrentUser.role.equals("admin")) {
            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // add data to the database
                    Dao<SubContractorEntity, String> subDao = null;
                    try {
                        subDao = daoFactory.getSubContractorEntityDao();
                        String whereParameter = projectEntity.id.toString().trim();
                        GenericRawResults<String[]> rawResults = subDao.queryRaw(QUERY_DELETE_SUB_CONTRACTOR_ASSIGN + whereParameter);

                        for (int i = 0; i < list.size(); i++) {
                            if (selectedList.get(i) == true) {
                                SubContractorEntity subEntity = list.get(i);
                                String insertParamenter = " (" + projectEntity.id + "," + subEntity.subContracotr_id+ ")";
                                subDao.queryRaw(QUERY_INSERT_SUB_CONTRACTOR_ASSIGN + insertParamenter);
                            }
                        }


                    } catch (SQLException e) {
                        throw new Error (e);
                    }

                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SubContractorSelectedListFragment(projectEntity));
                }
            });
        } else {
            imgAdd.setVisibility(View.GONE);
        }
    }

    private void bindData() {
        try {
            selectedList = new ArrayList<Boolean>();
            list = new ArrayList<>();
            List<SubContractorEntity> selectedSubContractorList = new ArrayList<>();

         /*   for (int i = 0; i < 5; i++) {
                SubContractorEntity entity = new SubContractorEntity();
                entity.name = "Sub-Contractor " + (i + 1);
                entity.phone = "0979516247" + (i + 1);
                entity.email = "Sub-Contractor" + (i + 1) + "@gmail.com";
                entity.address = "Yangon " + (i + 1);

                selectedList.add(false);
                list.add(entity);
            }
*/

            Dao<SubContractorEntity, String> subDao = daoFactory.getSubContractorEntityDao();

            String whereParameter = projectEntity.id.toString().trim();

            GenericRawResults<String[]> rawResults = subDao.queryRaw(QUERY_SELECT_SUB_CONTRACTOR_ASSIGN + whereParameter);

            selectedSubContractorList = ConvertToObject(rawResults);
            rawResults.close();
            // page through the results


            list = subDao.queryForAll();

            for (int i = 0; i < list.size(); i++) {
                selectedList.add(false);
                SubContractorEntity sub = list.get(i);
                for (int j = 0; j < selectedSubContractorList.size(); j++) {
                    SubContractorEntity selectedSub = selectedSubContractorList.get(j);
                    if (selectedSub.subContracotr_id.equals(sub.subContracotr_id)) {
                        selectedList.set(i, true);
                        break;
                    }
                }
            }

            adapter = new SubContractorSelectBoxListAdapter(this, view.getContext(), getActivity(), list, selectedList);

            SwipeMenuListView listView = (SwipeMenuListView) view.findViewById(R.id.listView);
            ColorDrawable myColor = new ColorDrawable(
                    this.getResources().getColor(R.color.color_accent)
            );
            listView.setDivider(myColor);
            listView.setDividerHeight(1);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private List<SubContractorEntity> ConvertToObject(GenericRawResults<String[]> rawResults) {
        List<SubContractorEntity> subContractorEntities = new ArrayList<>();
        for (String[] resultArray : rawResults) {
            SubContractorEntity subContractorEntity = new SubContractorEntity();
            subContractorEntity.subContracotr_id = resultArray[SubContractorEntity.COLUMN_ID];
            subContractorEntity.name = resultArray[SubContractorEntity.COLUMN_NAME];
            subContractorEntity.phone = resultArray[SubContractorEntity.COLUMN_PHONE];
            subContractorEntity.email = resultArray[SubContractorEntity.COLUMN_EMAIL];
            subContractorEntity.address = resultArray[SubContractorEntity.COLUMN_ADDRESS];
            subContractorEntities.add(subContractorEntity);
        }
        return subContractorEntities;
    }


    public void selectCheckBox(View v) {
        CheckBox cb = (CheckBox) v;
        selectedList.set(cb.getId(), cb.isChecked());

        if (hasSelectedItem()) {
            imgAdd.setVisibility(View.VISIBLE);
        } else {
            imgAdd.setVisibility(View.GONE);
        }
    }

    private boolean hasSelectedItem() {
        for (int i = 0; i < this.selectedList.size(); i++) {
            if (this.selectedList.get(i)) {
                return true;
            }
        }

        return false;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
