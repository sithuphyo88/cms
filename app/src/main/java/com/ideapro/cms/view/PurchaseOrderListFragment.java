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
import com.ideapro.cms.data.PurchaseOrderEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.PurchaseOrderListAdapter;
import com.ideapro.cms.view.swipeMenu.SwipeMenu;
import com.ideapro.cms.view.swipeMenu.SwipeMenuCreator;
import com.ideapro.cms.view.swipeMenu.SwipeMenuItem;
import com.ideapro.cms.view.swipeMenu.SwipeMenuListView;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseOrderListFragment extends Fragment {

    View view;
    ProjectEntity projectEntity;
    List<PurchaseOrderEntity> list;
    PurchaseOrderListAdapter adapter;
    private DaoFactory daoFactory;

    public PurchaseOrderListFragment() {
        this.projectEntity = new ProjectEntity();
    }

    public PurchaseOrderListFragment(ProjectEntity projectEntity) {
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
        view = inflater.inflate(R.layout.fragment_site_list, container, false);
        setHasOptionsMenu(true);
        daoFactory = new DaoFactory(view.getContext());

        bindData();
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_purchase_orders) + " for " + this.projectEntity.name);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void initializeUI() {
        ImageButton imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);

        if(CommonUtils.CurrentUser.role.equals("admin")) {
            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PurchaseOrderEntity orderEntity = new PurchaseOrderEntity();
                    orderEntity.purchaseOrderNo = "PO-0";
                    orderEntity.projectId = projectEntity.id;
                    orderEntity.date = "2016-04-28";
                    orderEntity.receivedNumber = 5;
                    orderEntity.purchasedNumber = 5;
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderAddFragment(projectEntity, orderEntity));
                }
            });
        } else {
            imgAdd.setVisibility(View.GONE);
        }
    }

    private void bindData() {
        try {
            list = new ArrayList<>();
/*
            PurchaseOrderEntity zentity = new PurchaseOrderEntity();
            zentity.purchaseOrderNo = "PO-0";
            zentity.date = "2016-04-28";
            zentity.receivedNumber = 5;
            zentity.purchasedNumber = 5;
            list.add(zentity);

            for (int i = 0; i < 30; i++) {
                PurchaseOrderEntity entity = new PurchaseOrderEntity();
                entity.purchaseOrderNo = "PO-" + (i + 1);
                entity.date = "2016-05-" + (i + 1);
                entity.receivedNumber = i + 1;
                entity.purchasedNumber = i + 2;

                list.add(entity);
            }
*/
            Dao<PurchaseOrderEntity, String> purchaseOrderEntityDao = daoFactory.getPurchaseOrderEntityDao();
            list = purchaseOrderEntityDao.queryBuilder().where().eq(PurchaseOrderEntity.PROJECT_ID, projectEntity.id).query();


            adapter = new PurchaseOrderListAdapter(view.getContext(), getActivity(), list);
            SwipeMenuListView listView = (SwipeMenuListView)view.findViewById(R.id.listView);
            ColorDrawable myColor = new ColorDrawable(
                    this.getResources().getColor(R.color.color_accent)
            );
            listView.setDivider(myColor);
            listView.setDividerHeight(1);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            if(CommonUtils.CurrentUser.role.equals("admin")) {
                SwipeMenuCreator creator = new SwipeMenuCreator() {
                    @Override
                    public void create(SwipeMenu menu) {
                        SwipeMenuItem editItem = new SwipeMenuItem(view.getContext());
                        editItem.setWidth(dp2px(50));
                        editItem.setTitle("edit");
                        editItem.setIcon(R.drawable.ic_edit);
                        menu.addMenuItem(editItem);

                        SwipeMenuItem deleteItem = new SwipeMenuItem(view.getContext());
                        deleteItem.setWidth(dp2px(50));
                        deleteItem.setTitle("delete");
                        deleteItem.setIcon(R.drawable.ic_delete);
                        menu.addMenuItem(deleteItem);

                        SwipeMenuItem approveItem = new SwipeMenuItem(view.getContext());
                        approveItem.setWidth(dp2px(50));
                        approveItem.setTitle("approve");
                        approveItem.setIcon(R.drawable.ic_approve);
                        menu.addMenuItem(approveItem);
                    }
                };

                listView.setMenuCreator(creator);
                listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

                listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                        switch (index) {
                            case 0:
                                // edit
                                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderAddFragment(projectEntity, list.get(position)));
                                break;

                            case 1:
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

                            case 2:
                                // edit
                                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderAddFragment(projectEntity, list.get(position)));
                                break;
                        }
                        // false : close the menu; true : not close the menu
                        return false;
                    }
                });
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderAddFragment(projectEntity, list.get(position)));
                    /*UserEntity userEntity = (UserEntity)parent.getItemAtPosition(position);
                    if(userEntity.userCode.equals("") == false) {
                        UserAddFragment userAddFragment = new UserAddFragment(userEntity);
                        CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), userAddFragment);
                    }*/
                }
            });

        } catch (Exception e){
            throw new Error(e);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
