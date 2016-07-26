package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.PurchaseOrderEntity;
import com.ideapro.cms.data.PurchaseOrderItemEntity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseOrderItemAddFragment extends Fragment {

    View view;
    ProjectEntity projectEntity;
    PurchaseOrderEntity purchaseOrderEntity;
    PurchaseOrderItemEntity purchaseOrderItemEntity;
    EditText txtPurchaseOrderDate;
    EditText txtTargetedDate;
    Spinner spnMaterialCategory;
    Spinner spnMaterial;
    Spinner spnUOM;
    EditText txtQuantity;
    EditText txtReceivedQuantity;
    EditText txtRemarks;

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
        txtPurchaseOrderDate = (EditText) view.findViewById(R.id.txtPurchaseOrderDate);
        txtTargetedDate = (EditText) view.findViewById(R.id.txtTargetedDate);
        spnMaterialCategory = (Spinner) view.findViewById(R.id.spnMaterialCategory);
        spnMaterial = (Spinner) view.findViewById(R.id.spnMaterial);
        spnUOM = (Spinner) view.findViewById(R.id.spnUOM);
        txtQuantity = (EditText) view.findViewById(R.id.txtQuantity);
        txtReceivedQuantity = (EditText) view.findViewById(R.id.txtReceivedQuantity);
        txtRemarks = (EditText) view.findViewById(R.id.txtRemarks);

        if (purchaseOrderItemEntity != null) {
            txtPurchaseOrderDate.setText(purchaseOrderItemEntity.purchaseOrderDate);
            txtTargetedDate.setText(purchaseOrderItemEntity.targetedDate);
            txtQuantity.setText(String.valueOf(purchaseOrderItemEntity.orderedQuantity));
            txtReceivedQuantity.setText(String.valueOf(purchaseOrderItemEntity.receivedQuantity));
            txtRemarks.setText(purchaseOrderItemEntity.remarks);
        }

        ArrayAdapter<String> mcAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_textview,
                getActivity().getResources().getStringArray(R.array.materialCategories));
        mcAdapter.setDropDownViewResource(R.layout.spinner_textview);
        spnMaterialCategory.setAdapter(mcAdapter);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_textview,
                getActivity().getResources().getStringArray(R.array.materials));
        mAdapter.setDropDownViewResource(R.layout.spinner_textview);
        spnMaterial.setAdapter(mAdapter);

        ArrayAdapter<String> uomAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_textview,
                getActivity().getResources().getStringArray(R.array.uoms));
        uomAdapter.setDropDownViewResource(R.layout.spinner_textview);
        spnUOM.setAdapter(uomAdapter);
    }
}
