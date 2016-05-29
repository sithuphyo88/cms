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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

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
public class PurchaseOrderItemAddFragment extends Fragment {

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    PurchaseOrderEntity purchaseOrderEntity;
    PurchaseOrderItemEntity purchaseOrderItemEntity;
    Spinner spnSupplier;
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
        spnSupplier = (Spinner) view.findViewById(R.id.spnSupplier);
        spnMaterialCategory = (Spinner) view.findViewById(R.id.spnMaterialCategory);
        spnMaterial = (Spinner) view.findViewById(R.id.spnMaterial);
        spnUOM = (Spinner) view.findViewById(R.id.spnUOM);
        txtQuantity = (EditText) view.findViewById(R.id.txtQuantity);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item,
                getActivity().getResources().getStringArray(R.array.suppliers));
        spnSupplier.setAdapter(adapter);

        ArrayAdapter<String> mcAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item,
                getActivity().getResources().getStringArray(R.array.materialCategories));
        spnMaterialCategory.setAdapter(mcAdapter);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item,
                getActivity().getResources().getStringArray(R.array.materials));
        spnMaterial.setAdapter(mAdapter);

        ArrayAdapter<String> uomAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item,
                getActivity().getResources().getStringArray(R.array.uoms));
        spnUOM.setAdapter(uomAdapter);
    }
}
