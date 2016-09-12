package com.ideapro.cms.view;


import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.DatabaseHelper;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.PurchaseOrderEntity;
import com.ideapro.cms.data.PurchaseOrderItemEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.PurchaseOrderItemListAdapter;
import com.ideapro.cms.view.swipeMenu.SwipeMenu;
import com.ideapro.cms.view.swipeMenu.SwipeMenuCreator;
import com.ideapro.cms.view.swipeMenu.SwipeMenuItem;
import com.ideapro.cms.view.swipeMenu.SwipeMenuListView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseOrderAddFragment extends Fragment {

    private static final String QUERY_SELECT_PURCHASE_ORDER_ITEM = "SELECT poi.id\n" +
            ",purchaseOrderId\n" +
            ",purchaseOrderDate\n" +
            ",targetedDate\n" +
            ",mc.name as materialCategory\n" +
            ",m.name as materialItem\n" +
            ",u.name as uom\n" +
            ",orderedQuantity\n" +
            ",receivedQuantity\n" +
            ",remarks\n" +
            "FROM purchaseOrderItem poi\n" +
            "INNER JOIN materialCategory mc ON\n" +
            "mc.id = poi.materialCategory\n" +
            "INNER JOIN material m ON\n" +
            "m.id= poi.materialItem\n" +
            "INNER JOIN UOM u ON\n" +
            "u.id= poi.uom";

    View view;
    ProjectEntity projectEntity;
    PurchaseOrderEntity purchaseOrderEntity;
    List<PurchaseOrderItemEntity> list;
    PurchaseOrderItemListAdapter adapter;
    EditText txtPurchaseOrderNo;
    EditText txtPurchaseOrderDate;
    SwipeMenuListView lstItems;
    Menu menu;

    private DaoFactory daoFactory;

    public PurchaseOrderAddFragment() {
        this.projectEntity = new ProjectEntity();
        this.purchaseOrderEntity = new PurchaseOrderEntity();
    }

    public PurchaseOrderAddFragment(ProjectEntity projectEntity, PurchaseOrderEntity purchaseOrderEntity) {
        if (projectEntity == null) {
            this.projectEntity = new ProjectEntity();
            this.purchaseOrderEntity = new PurchaseOrderEntity();
        } else {
            this.projectEntity = projectEntity;
            this.purchaseOrderEntity = purchaseOrderEntity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_purchase_order_add, container, false);
        this.menu = menu;
        daoFactory = new DaoFactory(view.getContext());

        initializeUI();
        bindData();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_purchase_order_details) + " for " + this.purchaseOrderEntity.purchaseOrderNo);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        txtPurchaseOrderNo = (EditText) view.findViewById(R.id.txtPurchaseOrderNo);
        txtPurchaseOrderDate = (EditText) view.findViewById(R.id.txtPurchaseOrderDate);
        lstItems = (SwipeMenuListView) view.findViewById(R.id.lstItems);

        ImageButton imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    getDataProjectOrder();
                    try {
                        Dao<PurchaseOrderEntity, String> purchaseOrderEntityDao = daoFactory.getPurchaseOrderEntityDao();
                        if (purchaseOrderEntity.id == null) {
                            purchaseOrderEntity.id = UUID.randomUUID().toString();
                            purchaseOrderEntityDao.create(purchaseOrderEntity);
                        } else {
                            purchaseOrderEntityDao.update(purchaseOrderEntity);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    PurchaseOrderItemEntity entity = new PurchaseOrderItemEntity();
                    entity.id = "";
                    entity.purchaseOrderId = purchaseOrderEntity.id;
                    entity.purchaseOrderDate = "";
                    entity.targetedDate = "";
                    entity.materialCategory = "MC1";
                    entity.materialItem = "M1";
                    entity.uom = "UOM1";
                    entity.receivedQuantity = 1;
                    entity.orderedQuantity = 1;
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderItemAddFragment(projectEntity, purchaseOrderEntity, entity));
                }
            }

            private void getDataProjectOrder() {
                purchaseOrderEntity.purchaseOrderNo = txtPurchaseOrderNo.getText().toString();
                purchaseOrderEntity.date = txtPurchaseOrderDate.getText().toString();
            }
        });
    }

    private boolean validation() {
        boolean flag = true;
        if (TextUtils.isEmpty(txtPurchaseOrderDate.getText().toString().trim())) {
            flag = false;
            txtPurchaseOrderDate.setError(getString(R.string.error_missing_PurchaseOrderDate));
        }

        if (TextUtils.isEmpty(txtPurchaseOrderNo.getText().toString().trim())) {
            flag = false;
            txtPurchaseOrderNo.setError(getString(R.string.error_missing_PurchaseOrderNo));
        }

        return flag;
    }

    private void bindData() {
        try {
            list = new ArrayList<>();
            this.txtPurchaseOrderNo.setText(purchaseOrderEntity.purchaseOrderNo);
            this.txtPurchaseOrderDate.setText(purchaseOrderEntity.date);

            /*for (int i = 0; i < 30; i++) {
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
*/
            if (purchaseOrderEntity.id != null) {
                Dao<PurchaseOrderItemEntity, String> purchaseOrderEntityDao = daoFactory.getPurchaseOrderItemDao();

                String whereParameter = " WHERE poi.purchaseOrderId = ?";
                String[] args = new String[]{purchaseOrderEntity.id.toString()};
            /*purchaseOrderEntityDao.queryRaw()*/
                GenericRawResults<String[]> rawResults = purchaseOrderEntityDao.queryRaw(QUERY_SELECT_PURCHASE_ORDER_ITEM + whereParameter, args);

                list = ConvertToObject(rawResults);
                rawResults.close();
                // page through the results
            }

            adapter = new PurchaseOrderItemListAdapter(view.getContext(), getActivity(), list);
            ColorDrawable myColor = new ColorDrawable(
                    this.getResources().getColor(R.color.color_accent)
            );
            lstItems.setDivider(myColor);
            lstItems.setDividerHeight(1);
            lstItems.setAdapter(adapter);
            adapter.notifyDataSetChanged();

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
                    editItem.setTitle("delete");
                    deleteItem.setIcon(R.drawable.ic_delete);
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
                            CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderItemAddFragment(projectEntity, purchaseOrderEntity, list.get(position)));
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
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderItemAddFragment(projectEntity, purchaseOrderEntity, list.get(position)));
                    /*UserEntity userEntity = (UserEntity)parent.getItemAtPosition(position);
                    if(userEntity.userCode.equals("") == false) {
                        UserAddFragment userAddFragment = new UserAddFragment(userEntity);
                        CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), userAddFragment);
                    }*/
                }
            });

        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private List<PurchaseOrderItemEntity> ConvertToObject(GenericRawResults<String[]> rawResults) {
        List<PurchaseOrderItemEntity> tmplist = new ArrayList<>();
        for (String[] resultArray : rawResults) {
            PurchaseOrderItemEntity entity = new PurchaseOrderItemEntity();
            entity.id = resultArray[entity.COLUMN_ID];                                                  //1
            entity.purchaseOrderId = resultArray[entity.COLUMN_PURCHASE_ORDER_ID];                 //2
            entity.purchaseOrderDate = resultArray[entity.COLUMN_PURCHASE_ORDER_DATE];            //3
            entity.targetedDate = resultArray[entity.COLUMN_TARGETED_DATE];                         //4
            entity.materialCategory = resultArray[entity.COLUMN_MATERIAL_CATEGORY];                //5
            entity.materialItem = resultArray[entity.COLUMN_MATERIAL_ITEM];                         //6
            entity.uom = resultArray[entity.COLUMN_UOM];                                                //7
            entity.orderedQuantity = Integer.parseInt(resultArray[entity.COLUMN_ORDER_QUANTITY]);   //8
            entity.receivedQuantity = Integer.parseInt(resultArray[entity.COLUMN_RECEIVE_QUANTITY]);//9
            entity.remarks = resultArray[entity.COLUMN_REMARKS];                                         //10

            tmplist.add(entity);
        }
        return tmplist;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
