package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.utils.CommonUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class SiteAddFragment extends Fragment implements View.OnClickListener {

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

    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

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

        if (proProgress != null) {
            proProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteProgressListFragment(projectEntity, siteEntity));
                }
            });
        }

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);
        fab_open = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(view.getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(view.getContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;

            case R.id.fab1:
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new EngineerListFragment(projectEntity, siteEntity));
                break;

            case R.id.fab2:
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderListFragment(projectEntity, siteEntity));
                break;

            case R.id.fab3:
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new BluePrintListFragment(projectEntity, siteEntity));
                break;
        }
    }

    public void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;
            Log.d("Raj", "open");

        }
    }
}
