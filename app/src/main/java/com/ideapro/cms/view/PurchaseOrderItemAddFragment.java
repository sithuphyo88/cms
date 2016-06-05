package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.PurchaseOrderEntity;
import com.ideapro.cms.data.PurchaseOrderItemEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.utils.CommonUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseOrderItemAddFragment extends Fragment {

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    PurchaseOrderEntity purchaseOrderEntity;
    PurchaseOrderItemEntity purchaseOrderItemEntity;
    EditText txtPurchaseOrderDate;
    EditText txtTargetedDate;
    Spinner spnMaterialCategory;
    Spinner spnMaterial;
    Spinner spnUOM;
    EditText txtQuantity;

    public PurchaseOrderItemAddFragment() {
        this.projectEntity = new ProjectEntity();
        this.siteEntity = new SiteEntity();
        this.purchaseOrderEntity = new PurchaseOrderEntity();
    }

    public PurchaseOrderItemAddFragment(ProjectEntity projectEntity, SiteEntity siteEntity, PurchaseOrderEntity purchaseOrderEntity, PurchaseOrderItemEntity purchaseOrderItemEntity) {
        if(siteEntity == null) {
            this.projectEntity = new ProjectEntity();
            this.siteEntity = new SiteEntity();
            this.purchaseOrderEntity = new PurchaseOrderEntity();
            this.purchaseOrderItemEntity = new PurchaseOrderItemEntity();
        } else {
            this.projectEntity = projectEntity;
            this.siteEntity = siteEntity;
            this.purchaseOrderEntity = purchaseOrderEntity;
            this.purchaseOrderItemEntity = purchaseOrderItemEntity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_purchase_order_item_add, container, false);
        initializeUI();
        setActionBar();
        return view;
    }

    private void setActionBar() {
        CommonUtils.setActionBarForFragment((ActionBarActivity)getActivity(),
                getString(R.string.label_purchase_order_item_details),
                R.mipmap.ic_done);
    }

    private void initializeUI() {
        txtPurchaseOrderDate = (EditText) view.findViewById(R.id.txtPurchaseOrderDate);
        txtTargetedDate = (EditText) view.findViewById(R.id.txtTargetedDate);
        spnMaterialCategory = (Spinner) view.findViewById(R.id.spnMaterialCategory);
        spnMaterial = (Spinner) view.findViewById(R.id.spnMaterial);
        spnUOM = (Spinner) view.findViewById(R.id.spnUOM);
        txtQuantity = (EditText) view.findViewById(R.id.txtQuantity);

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
