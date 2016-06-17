package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.utils.CommonUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class SiteAddFragment extends Fragment { //implements View.OnClickListener

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    Button butTaskList;
    EditText txtSiteName;
    EditText txtAddress;
    EditText txtStartDate;
    EditText txtEndDate;

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

            butTaskList = (Button) view.findViewById(R.id.butTaskList);
            butTaskList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteTaskListFragment(projectEntity, siteEntity));
                }
            });

            Button butComment = (Button) view.findViewById(R.id.butComment);
            butComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new CommentListFragment());
                }
            });
        }
    }
}
