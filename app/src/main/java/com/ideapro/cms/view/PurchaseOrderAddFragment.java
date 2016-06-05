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
import android.widget.EditText;
import android.widget.ImageButton;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.PurchaseOrderEntity;
import com.ideapro.cms.data.PurchaseOrderItemEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.PurchaseOrderItemListAdapter;
import com.ideapro.cms.view.swipeMenu.SwipeMenu;
import com.ideapro.cms.view.swipeMenu.SwipeMenuCreator;
import com.ideapro.cms.view.swipeMenu.SwipeMenuItem;
import com.ideapro.cms.view.swipeMenu.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseOrderAddFragment extends Fragment {

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    PurchaseOrderEntity purchaseOrderEntity;
    List<PurchaseOrderItemEntity> list;
    PurchaseOrderItemListAdapter adapter;
    EditText txtPurchaseOrderNo;
    EditText txtPurchaseOrderDate;
    SwipeMenuListView lstItems;

    public PurchaseOrderAddFragment() {
        this.projectEntity = new ProjectEntity();
        this.siteEntity = new SiteEntity();
        this.purchaseOrderEntity = new PurchaseOrderEntity();
    }

    public PurchaseOrderAddFragment(ProjectEntity projectEntity, SiteEntity siteEntity, PurchaseOrderEntity purchaseOrderEntity) {
        if(siteEntity == null) {
            this.projectEntity = new ProjectEntity();
            this.siteEntity = new SiteEntity();
            this.purchaseOrderEntity = new PurchaseOrderEntity();
        } else {
            this.projectEntity = projectEntity;
            this.siteEntity = siteEntity;
            this.purchaseOrderEntity = purchaseOrderEntity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_purchase_order_add, container, false);
        initializeUI();
        bindData();
        setActionBar();
        return view;
    }

    private void setActionBar() {
        CommonUtils.setActionBarForFragment((ActionBarActivity)getActivity(),
                getString(R.string.label_purchase_order_details) + " for " + this.purchaseOrderEntity.purchaseOrderNo,
                R.mipmap.ic_done);
    }

    private void initializeUI() {
        txtPurchaseOrderNo = (EditText) view.findViewById(R.id.txtPurchaseOrderNo);
        txtPurchaseOrderDate = (EditText) view.findViewById(R.id.txtPurchaseOrderDate);
        lstItems = (SwipeMenuListView) view.findViewById(R.id.lstItems);

        ImageButton imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PurchaseOrderItemEntity entity = new PurchaseOrderItemEntity();
                entity.purchaseOrderDate = "2016-06-05";
                entity.targetedDate = "2016-06-06";
                entity.materialCategory = "MC1";
                entity.materialItem = "M1";
                entity.uom = "UOM1";
                entity.receivedQuantity = 1;
                entity.orderedQuantity = 1;

                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderItemAddFragment(projectEntity, siteEntity, purchaseOrderEntity, entity));
            }
        });
    }

    private void bindData() {
        try {
            list = new ArrayList<>();
            this.txtPurchaseOrderNo.setText(purchaseOrderEntity.purchaseOrderNo);
            this.txtPurchaseOrderDate.setText(purchaseOrderEntity.date);

            for (int i = 0; i < 30; i++) {
                PurchaseOrderItemEntity entity = new PurchaseOrderItemEntity();
                entity.purchaseOrderDate = "2016-06-" + (i + 1);
                entity.targetedDate = "2016-06-" + (i + 2);
                entity.materialCategory = "MC" + (i + 1);
                entity.materialItem = "M" + (i + 1);
                entity.uom = "UOM" + (i + 1);
                entity.orderedQuantity = i + 2;
                entity.receivedQuantity = i + 1;

                list.add(entity);
            }

            adapter = new PurchaseOrderItemListAdapter(view.getContext(), getActivity(), list);
            lstItems.setDivider(null);
            lstItems.setAdapter(adapter);
            adapter.notifyDataSetChanged();

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

            lstItems.setMenuCreator(creator);
            lstItems.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

            lstItems.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
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

            lstItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderItemAddFragment(projectEntity, siteEntity, purchaseOrderEntity, list.get(position)));
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
