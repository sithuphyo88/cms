package com.ideapro.cms.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.data.TaskEntity;
import com.ideapro.cms.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SiteTaskAddFragment extends Fragment {

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    TaskEntity taskEntity;
    Button butEvidence;
    Button butBluePrint;
    Button butComment;

    @BindView(R.id.txtTitle)
    EditText txtTitle;

    @BindView(R.id.txtDescription)
    EditText txtDescritpion;

    @BindView(R.id.txtStartDate)
    EditText txtStartDate;

    @BindView(R.id.txtEndDate)
    EditText txtEndDate;

    @BindView(R.id.tvwAssignee)
    TextView tvwAssignee;

    public SiteTaskAddFragment() {
        // Required empty public constructor
    }

    public SiteTaskAddFragment(ProjectEntity projectEntity, SiteEntity siteEntity, TaskEntity taskEntity) {
        if (projectEntity == null) {
            this.projectEntity = new ProjectEntity();
            this.siteEntity = new SiteEntity();
            this.taskEntity = new TaskEntity();
        } else {
            this.projectEntity = projectEntity;
            this.siteEntity = siteEntity;
            this.taskEntity = taskEntity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_site_task_add, container, false);
        setHasOptionsMenu(true);

        // thrid party library for findviewbyID
        ButterKnife.bind(this, view);
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_site_task_entry) + " for " + this.siteEntity.name);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        // title
        txtTitle.setText(this.taskEntity.title);
        // address
        txtDescritpion.setText(this.taskEntity.description);
        // start Date
        txtStartDate.setText(this.taskEntity.startDate);
        // end Date
        txtEndDate.setText(this.taskEntity.endDate);
        // asssingee
        tvwAssignee.setText(this.taskEntity.assignee);

        butEvidence = (Button) view.findViewById(R.id.butEvidence);
        butEvidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteProgressListFragment(projectEntity, siteEntity));
            }
        });

        butBluePrint = (Button) view.findViewById(R.id.butBluePrint);
        butBluePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new BluePrintListFragment(projectEntity, siteEntity));
            }
        });

        butComment = (Button) view.findViewById(R.id.butComment);
        butComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new CommentListFragment());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
        }
    }
}
