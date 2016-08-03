package com.ideapro.cms.view;


import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SubContractorEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.SubContractorListAdapter;
import com.ideapro.cms.view.swipeMenu.SwipeMenu;
import com.ideapro.cms.view.swipeMenu.SwipeMenuCreator;
import com.ideapro.cms.view.swipeMenu.SwipeMenuItem;
import com.ideapro.cms.view.swipeMenu.SwipeMenuListView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubContractorSelectedListFragment extends Fragment {

    private static final String QUERY_SELECT_SUB_CONTRACTOR_ASSIGN = "SELECT subContracotr_id\n" +
            "\t\t\t, name\n" +
            "\t\t\t, phone\n" +
            "\t\t\t, email\n" +
            "\t\t\t, address\n" +
            "FROM subContractor s\n" +
            "INNER JOIN projects_sub_contractors ps\n" +
            "ON ps.sub_contractor_id = s.subContracotr_id \n" +
            "WHERE ps.project_id= ";
    View view;
    List<SubContractorEntity> list;
    SubContractorListAdapter adapter;
    DaoFactory daoFactory;
    ProjectEntity projectEntity;


    public SubContractorSelectedListFragment(ProjectEntity projectEntity) {
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
        view = inflater.inflate(R.layout.fragment_sub_contractor_list, container, false);
        setHasOptionsMenu(true);

        daoFactory = new DaoFactory(view.getContext());

        bindData();
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_selected_sub_contractor));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        ImageButton imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);

        if (CommonUtils.CurrentUser.role.equals("admin")) {
            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SubContractorSelectedBoxListFragment(projectEntity));
                }
            });
        } else {
            imgAdd.setVisibility(View.GONE);
        }
    }

    private void bindData() {
        try {
            list = new ArrayList<>();

            /*for (int i = 0; i < 5; i++) {
                SubContractorEntity entity = new SubContractorEntity();
                entity.name = "Sub-Contractor " + (i + 1);
                entity.phone = "0979516247" + (i + 1);
                entity.email = "Sub-Contractor" + (i + 1) + "@gmail.com";
                entity.address = "Yangon " + (i + 1);

                list.add(entity);
            }*/

            // get data from database

            Dao<SubContractorEntity, String> subDao = daoFactory.getSubContractorEntityDao();

            String whereParameter = projectEntity.id.toString().trim();

            GenericRawResults<String[]> rawResults = subDao.queryRaw(QUERY_SELECT_SUB_CONTRACTOR_ASSIGN + whereParameter);

            list = ConvertToObject(rawResults);
            rawResults.close();
            // page through the results

            adapter = new SubContractorListAdapter(view.getContext(), getActivity(), list);

            SwipeMenuListView listView = (SwipeMenuListView) view.findViewById(R.id.listView);
            ColorDrawable myColor = new ColorDrawable(
                    this.getResources().getColor(R.color.color_accent)
            );
            listView.setDivider(myColor);
            listView.setDividerHeight(1);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    SwipeMenuItem deleteItem = new SwipeMenuItem(view.getContext());
                    deleteItem.setWidth(dp2px(50));
                    deleteItem.setTitle("delete");
                    deleteItem.setIcon(R.drawable.ic_delete);
                    menu.addMenuItem(deleteItem);
                }
            };

            listView.setMenuCreator(creator);
            listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                    switch (index) {
                        case 0:
                            // delete
                            CommonUtils.showConfirmDialogBox(getActivity(), getString(R.string.message_confirmation_delete),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            list.remove(position);
                                            adapter.notifyDataSetChanged();
                                        }
                                    },
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    });
                            break;
                    }
                    // false : close the menu; true : not close the menu
                    return false;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SubContractorCashFlowListFragment(projectEntity,list.get(position)));
                }
            });

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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
