package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.MaterialCategoryEntity;
import com.ideapro.cms.data.MaterialEntity;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.PurchaseOrderEntity;
import com.ideapro.cms.data.PurchaseOrderItemEntity;
import com.ideapro.cms.data.UnitOfMeasurementEntity;
import com.j256.ormlite.dao.Dao;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseOrderItemAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    View view;
    ProjectEntity projectEntity;
    PurchaseOrderEntity purchaseOrderEntity;
    PurchaseOrderItemEntity purchaseOrderItemEntity;

    @BindView(R.id.txtPurchaseOrderDate)
    EditText txtPurchaseOrderDate;

    @BindView(R.id.txtTargetedDate)
    EditText txtTargetedDate;

    @BindView(R.id.spnMaterialCategory)
    Spinner spnMaterialCategory;

    @BindView(R.id.spnMaterial)
    Spinner spnMaterial;

    @BindView(R.id.spnUOM)
    Spinner spnUOM;

    @BindView(R.id.txtQuantity)
    EditText txtQuantity;

    @BindView(R.id.txtReceivedQuantity)
    EditText txtReceivedQuantity;

    @BindView(R.id.txtRemarks)
    EditText txtRemarks;

    private DaoFactory daoFactory;
    List<MaterialCategoryEntity> mcList;
    List<UnitOfMeasurementEntity> uomList;
    List<MaterialEntity> mList;
    private boolean flag_update;

    public PurchaseOrderItemAddFragment() {
        this.projectEntity = new ProjectEntity();
        this.purchaseOrderEntity = new PurchaseOrderEntity();
    }

    public PurchaseOrderItemAddFragment(ProjectEntity projectEntity, PurchaseOrderEntity purchaseOrderEntity, PurchaseOrderItemEntity purchaseOrderItemEntity) {
        if (projectEntity == null) {
            this.projectEntity = new ProjectEntity();
            this.purchaseOrderEntity = new PurchaseOrderEntity();
            this.purchaseOrderItemEntity = new PurchaseOrderItemEntity();
        } else {
            this.projectEntity = projectEntity;
            this.purchaseOrderEntity = purchaseOrderEntity;
            this.purchaseOrderItemEntity = purchaseOrderItemEntity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_purchase_order_item_add, container, false);
        setHasOptionsMenu(true);
        daoFactory = new DaoFactory(view.getContext());
        ButterKnife.bind(this, view);

        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_purchase_order_item_details));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {

        // material category list
        mcList = new ArrayList<>();

        // unit of measurement list
        uomList = new ArrayList<>();

        // material list
        mList = new ArrayList<>();

        try {
            Dao<MaterialCategoryEntity, String> materialCategoryEntityDao = daoFactory.getMaterialCategoryEntityDao();
            Dao<UnitOfMeasurementEntity, String> uomDao = daoFactory.getUnitOfMeasurementDao();


            mcList = materialCategoryEntityDao.queryForAll();
            uomList = uomDao.queryForAll();
        } catch (SQLException e) {
            throw new Error(e);
        }
        MaterialCategoryEntity mcEntity = new MaterialCategoryEntity();
        String[] mcItems = new String[mcList.size()];
        String[] uomItems = new String[uomList.size()];
        String[] mItems = new String[]{"none"};

        // for material category list
        for (int i = 0; i < mcList.size(); i++) {
            mcItems[i] = mcList.get(i).name;
        }
        //for uom list
        for (int i = 0; i < uomList.size(); i++) {
            uomItems[i] = uomList.get(i).name;
        }

        ArrayAdapter<String> mcAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_textview,
                mcItems);
        mcAdapter.setDropDownViewResource(R.layout.spinner_textview);
        spnMaterialCategory.setAdapter(mcAdapter);

        spnMaterialCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {

                    mList = daoFactory.getMaterialEntityDao()
                            .queryBuilder()
                            .where()
                            .eq(MaterialEntity.MATERIAL_GROUP_ID, mcList.get(position).id)
                            .query();
                    String[] mItems = new String[mList.size()];

                    // for material list
                    for (int i = 0; i < mList.size(); i++) {
                        mItems[i] = mList.get(i).name;
                    }

                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_textview,
                            mItems);
                    mAdapter.setDropDownViewResource(R.layout.spinner_textview);
                    spnMaterial.setAdapter(mAdapter);

                } catch (SQLException e) {
                    throw new Error(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_textview,
                mItems);
        mAdapter.setDropDownViewResource(R.layout.spinner_textview);
        spnMaterial.setAdapter(mAdapter);

        ArrayAdapter<String> uomAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_textview,
                uomItems);
        uomAdapter.setDropDownViewResource(R.layout.spinner_textview);
        spnUOM.setAdapter(uomAdapter);

        DateOnClickListner();

        if (purchaseOrderItemEntity != null) {

            txtPurchaseOrderDate.setText(purchaseOrderItemEntity.purchaseOrderDate);
            txtTargetedDate.setText(purchaseOrderItemEntity.targetedDate);
            txtQuantity.setText(String.valueOf(purchaseOrderItemEntity.orderedQuantity));
            txtReceivedQuantity.setText(String.valueOf(purchaseOrderItemEntity.receivedQuantity));
            txtRemarks.setText(purchaseOrderItemEntity.remarks);
            spnMaterialCategory.setSelection(getMclSelectedPosition(mcList, purchaseOrderItemEntity.materialCategory));
            spnMaterial.setSelection(getMlSelectedPosition(mList, purchaseOrderItemEntity.materialItem));
            spnUOM.setSelection(getUomSelectedPosition(uomList, purchaseOrderItemEntity.uom));
        }
        if (!purchaseOrderItemEntity.id.isEmpty()) {
            flag_update = true;
        }


    }

    private int getMclSelectedPosition(List<MaterialCategoryEntity> mcList, String materialCategory) {
        int position = 0;
        for (int i = 0; i < mcList.size(); i++) {
            if (mcList.get(i).name.toString().equals(materialCategory)) {
                position = i;
                break;
            }
        }
        return position;
    }

    private int getUomSelectedPosition(List<UnitOfMeasurementEntity> uomList, String uom) {
        int position = 0;
        for (int i = 0; i < uomList.size(); i++) {
            if (uomList.get(i).name.toString().equals(uom.toString())) {
                position = i;
                break;
            }
        }
        return position;
    }

    private int getMlSelectedPosition(List<MaterialEntity> mList, String material) {
        int position = 0;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).name.toString().equals(material.toString())) {
                position = i;
                break;
            }
        }
        return position;
    }

    private void DateOnClickListner() {
        txtPurchaseOrderDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showOrderDatePicker();
                }
            }
        });

        txtPurchaseOrderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderDatePicker();
            }
        });

        txtTargetedDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTargetDatePicker();
                }
            }
        });

        txtTargetedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTargetDatePicker();
            }
        });
    }

    private void showOrderDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog thirdPartyDatePicker = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        thirdPartyDatePicker.show(getActivity().getFragmentManager(), "showOrderDatePicker");

    }

    private void showTargetDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog thirdPartyDatePicker = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        thirdPartyDatePicker.show(getActivity().getFragmentManager(), "showTargetDatePicker");

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(getContext(), "Year : " + year + " Month : " + monthOfYear + " Day : " + dayOfMonth, Toast.LENGTH_SHORT).show();
        if (view.getTag().toString().equals("showOrderDatePicker")) {
            txtPurchaseOrderDate.setText(year + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth));
        }
        if (view.getTag().toString().equals("showTargetDatePicker")) {
            txtTargetedDate.setText(year + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (validation()) {
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
        }

        return true;
    }

    private boolean validation() {
        boolean flag = true;

        // purchase date
        if (TextUtils.isEmpty(txtPurchaseOrderDate.getText().toString())) {
            flag = false;
            txtPurchaseOrderDate.setError(getString(R.string.error_missing_purchase_order_date_empty));
        }

        // target date
        if (TextUtils.isEmpty(txtTargetedDate.getText().toString())) {
            flag = false;
            txtTargetedDate.setError(getString(R.string.error_missing_purchase_target_date_empty));
        }

        // quantity
        if (TextUtils.isEmpty(txtQuantity.getText().toString())) {
            flag = false;
            txtQuantity.setError(getString(R.string.error_missing_purchase_quantiy_empty));
        }

        // receive quantity
        if (TextUtils.isEmpty(txtReceivedQuantity.getText().toString())) {
            flag = false;
            txtReceivedQuantity.setError(getString(R.string.error_missing_purchase_receive_quantiy_empty));
        }

        if (!TextUtils.isEmpty(txtReceivedQuantity.getText().toString()) && !TextUtils.isEmpty(txtQuantity.getText().toString())) {
            flag = false;
            int quantity = Integer.parseInt(String.valueOf(txtQuantity.getText()));
            int recieveQuantity = Integer.parseInt(String.valueOf(txtReceivedQuantity.getText()));

            if (recieveQuantity > quantity) {
                txtReceivedQuantity.setError(getString(R.string.error_missing_purchase_receive_more_than_quantity));
            }
        }

        return flag;
    }

    private void reset() {
        txtPurchaseOrderDate.setText("");
        txtTargetedDate.setText("");
        spnMaterialCategory.setSelection(0);
        spnMaterial.setSelection(0);
        spnUOM.setSelection(0);
        txtQuantity.setText("1");
        txtReceivedQuantity.setText("1");
        txtRemarks.setText("");
    }

    private void saveData() throws SQLException {
        Dao<PurchaseOrderItemEntity, String> poiDao = daoFactory.getPurchaseOrderItemDao();
        purchaseOrderItemEntity.id = UUID.randomUUID().toString();
        poiDao.create(purchaseOrderItemEntity);
    }

    private void updateData() throws SQLException {
        Dao<PurchaseOrderItemEntity, String> poiDao = daoFactory.getPurchaseOrderItemDao();
        poiDao.update(purchaseOrderItemEntity);
    }

    private void getData() {
        purchaseOrderItemEntity.purchaseOrderId = purchaseOrderItemEntity.purchaseOrderId.trim().toString();
        purchaseOrderItemEntity.purchaseOrderDate = txtPurchaseOrderDate.getText().toString();
        purchaseOrderItemEntity.targetedDate = txtTargetedDate.getText().toString();
        purchaseOrderItemEntity.materialCategory = (mcList.get(spnMaterialCategory.getSelectedItemPosition())).id.toString();
        purchaseOrderItemEntity.materialItem = (mList.get(spnMaterial.getSelectedItemPosition())).id.toString();
        purchaseOrderItemEntity.uom = (uomList.get(spnUOM.getSelectedItemPosition())).id.toString();
        purchaseOrderItemEntity.orderedQuantity = Integer.parseInt(txtQuantity.getText().toString());
        purchaseOrderItemEntity.receivedQuantity = Integer.parseInt(txtReceivedQuantity.getText().toString());
        purchaseOrderItemEntity.remarks = txtRemarks.getText().toString();
    }


}
