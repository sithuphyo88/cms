package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.data.SiteProgressHistoryEntity;
import com.ideapro.cms.utils.CommonUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class SiteAddFragment extends Fragment {

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    Button butAssignEngineers;
    Button butManageBluePrints;
    Button butManagePurchaseOrders;
    EditText txtSiteName;
    EditText txtAddress;
    EditText txtStartDate;
    EditText txtEndDate;
    ProgressBar proProgress;
    TextView tvwProgressValue;

    public SiteAddFragment() {
        // Required empty public constructor
    }

    public SiteAddFragment(ProjectEntity projectEntity, SiteEntity siteEntity) {
        if(projectEntity == null) {
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
        view = inflater.inflate(R.layout.fragment_site_add, container, false);
        setHasOptionsMenu(true);

        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_site_entry) + " for " + this.projectEntity.name);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void initializeUI() {
        butAssignEngineers = (Button) view.findViewById(R.id.butAssignEngineers);
        if(CommonUtils.CurrentUser.role.equals("admin")) {
            butAssignEngineers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new EngineerListFragment(projectEntity, siteEntity));
                }
            });
        } else {
            butAssignEngineers.setVisibility(View.GONE);
        }

        butManageBluePrints = (Button) view.findViewById(R.id.butManageBluePrints);
        if(CommonUtils.CurrentUser.role.equals("admin")) {
            butManageBluePrints.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new BluePrintListFragment(projectEntity, siteEntity));
                }
            });
        } else {
            butManageBluePrints.setText(getString(R.string.label_confirm_blue_print));
            butManageBluePrints.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SiteProgressHistoryEntity historyEntity = new SiteProgressHistoryEntity();
                    historyEntity.siteName = siteEntity.name;
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteBluePrintConfirmListFragment(historyEntity));
                }
            });
        }

        butManagePurchaseOrders = (Button) view.findViewById(R.id.butManagePurchaseOrders);
        if(CommonUtils.CurrentUser.role.equals("admin")) {
            butManagePurchaseOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderListFragment(projectEntity, siteEntity));
                }
            });
        } else {
            butManagePurchaseOrders.setVisibility(View.GONE);
        }

        if(this.siteEntity != null) {
            txtSiteName = (EditText) view.findViewById(R.id.txtSiteName);
            txtSiteName.setText(this.siteEntity.name);

            txtAddress = (EditText) view.findViewById(R.id.txtAddress);
            txtAddress.setText(this.siteEntity.address);

            txtStartDate = (EditText) view.findViewById(R.id.txtStartDate);
            txtStartDate.setText(this.siteEntity.startDate);

            txtEndDate = (EditText) view.findViewById(R.id.txtEndDate);
            txtEndDate.setText(this.siteEntity.endDate);

            proProgress = (ProgressBar) view.findViewById(R.id.proProgress);
            proProgress.setProgress(Integer.parseInt(this.siteEntity.progress));

            tvwProgressValue = (TextView) view.findViewById(R.id.tvwProgressValue);
            tvwProgressValue.setText(this.siteEntity.progress + " %");
        }

        proProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteProgressListFragment(projectEntity, siteEntity));
            }
        });
    }
}
