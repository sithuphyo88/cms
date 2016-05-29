package com.ideapro.cms.view;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.PurchaseOrderEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.PurchaseOrderListAdapter;
import com.ideapro.cms.view.listAdapter.SiteListAdapter;
import com.ideapro.cms.view.swipeMenu.SwipeMenu;
import com.ideapro.cms.view.swipeMenu.SwipeMenuCreator;
import com.ideapro.cms.view.swipeMenu.SwipeMenuItem;
import com.ideapro.cms.view.swipeMenu.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseOrderListFragment extends Fragment {

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    List<PurchaseOrderEntity> list;
    PurchaseOrderListAdapter adapter;

    public PurchaseOrderListFragment() {
        this.projectEntity = new ProjectEntity();
        this.siteEntity = new SiteEntity();
    }

    public PurchaseOrderListFragment(ProjectEntity projectEntity, SiteEntity siteEntity) {
        if(siteEntity == null) {
            this.projectEntity = new ProjectEntity();
            this.siteEntity = new SiteEntity();
        } else {
            this.projectEntity = projectEntity;
            this.siteEntity = siteEntity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_site_list, container, false);
        bindData();
        setActionBar();
        initializeUI();
        return view;
    }

    private void setActionBar() {
        CommonUtils.setActionBarForFragment((ActionBarActivity)getActivity(),
                getString(R.string.label_purchase_orders) + " for " + this.siteEntity.name,
                R.mipmap.ic_search);
    }

    private void initializeUI() {
        ImageButton imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);

        if(CommonUtils.CurrentUser.role.equals("admin")) {
            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PurchaseOrderEntity orderEntity = new PurchaseOrderEntity();
                    orderEntity.purchaseOrderNo = "Purchase Order No - 0";
                    orderEntity.date = "Purchase Order Date - 2016-04-28";
                    orderEntity.receivedNumber = "5";
                    orderEntity.purchasedNumber = "5";
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderAddFragment(projectEntity, siteEntity, orderEntity));
                }
            });
        } else {
            imgAdd.setVisibility(View.GONE);
        }
    }

    private void bindData() {
        try {
            list = new ArrayList<>();
            PurchaseOrderEntity zentity = new PurchaseOrderEntity();
            zentity.purchaseOrderNo = "Purchase Order No - 0";
            zentity.date = "Purchase Order Date - 2016-04-28";
            zentity.receivedNumber = "5";
            zentity.purchasedNumber = "5";
            list.add(zentity);

            for (int i = 0; i < 30; i++) {
                PurchaseOrderEntity entity = new PurchaseOrderEntity();
                entity.purchaseOrderNo = "Purchase Order No " + (i + 1);
                entity.date = "2016-05-" + (i + 1);
                entity.receivedNumber = String.valueOf(i);
                entity.purchasedNumber = String.valueOf(i + 1);

                list.add(entity);
            }

            adapter = new PurchaseOrderListAdapter(view.getContext(), getActivity(), list);
            SwipeMenuListView listView = (SwipeMenuListView)view.findViewById(R.id.listView);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            if(CommonUtils.CurrentUser.role.equals("admin")) {
                SwipeMenuCreator creator = new SwipeMenuCreator() {
                    @Override
                    public void create(SwipeMenu menu) {
                        SwipeMenuItem editItem = new SwipeMenuItem(view.getContext());
                        editItem.setWidth(dp2px(50));
                        editItem.setTitle("edit");
                        editItem.setIcon(R.mipmap.ic_edit);
                        menu.addMenuItem(editItem);

                        SwipeMenuItem deleteItem = new SwipeMenuItem(view.getContext());
                        deleteItem.setWidth(dp2px(50));
                        editItem.setTitle("delete");
                        deleteItem.setIcon(R.mipmap.ic_delete);
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
                                // edit
                                //CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteAddFragment(projectEntity, list.get(position)));
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
                        }
                        // false : close the menu; true : not close the menu
                        return false;
                    }
                });
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderAddFragment(projectEntity, siteEntity, list.get(position)));
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
