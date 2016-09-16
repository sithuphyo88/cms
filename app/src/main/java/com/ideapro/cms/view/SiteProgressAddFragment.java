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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SiteEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SiteProgressAddFragment extends Fragment {

    private static final String BARG_SITE_ID = "site_id";
    private static final String BARG_TASK_ID = "task_id";
    private static final String BARG_PROJECT_ID = "project_id";
    private static final String BARG_PROGRESS_ID = "progress_id";
    private static final String BARG_TASK_NAME = "task_name";

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;

    Button butAddEvidence;

    @BindView(R.id.txtSiteName)
    EditText txtSiteName;

    @BindView(R.id.txtProgress)
    EditText txtProgress;

    @BindView(R.id.txtDetailDescription)
    EditText txtDetailDescription;

    ProgressBar proProgress;
    TextView tvwProgressValue;

    public SiteProgressAddFragment() {
        // Required empty public constructor
    }

    public SiteProgressAddFragment(ProjectEntity projectEntity, SiteEntity siteEntity) {
        if (projectEntity == null) {
            this.projectEntity = new ProjectEntity();
            this.siteEntity = new SiteEntity();
        } else {
            this.projectEntity = projectEntity;
            this.siteEntity = siteEntity;
        }
    }

    public static SiteProgressAddFragment newInstance(String projectId, String siteId, String taskId, String taskName) {

        Bundle args = new Bundle();
        args.putString(BARG_PROJECT_ID, projectId);
        args.putString(BARG_SITE_ID, siteId);
        args.putString(BARG_TASK_ID, taskId);
        args.putString(BARG_TASK_NAME, taskId);

        SiteProgressAddFragment fragment = new SiteProgressAddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_site_progress_add, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        initializeUI();
        bindData();
        return view;
    }

    private void bindData() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_progress) + " for " + this.siteEntity.name);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        butAddEvidence = (Button) view.findViewById(R.id.butAddEvidence);
        butAddEvidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        if (this.siteEntity != null) {
            txtSiteName = (EditText) view.findViewById(R.id.txtSiteName);
            txtSiteName.setText(this.siteEntity.name);
        }

        txtSiteName.setText(getTaskName());
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.label_take_evidence)), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
        }
    }

    private String getProjectId() {
        Bundle bundle = getArguments();
        String projectId = "";
        if (bundle != null) {
            projectId = bundle.getString(BARG_PROJECT_ID);
        }
        return projectId;
    }

    private String getSiteId() {
        Bundle bundle = getArguments();
        String siteId = "";
        if (bundle != null) {
            siteId = bundle.getString(BARG_SITE_ID);
        }
        return siteId;
    }

    private String getTaskId() {
        Bundle bundle = getArguments();
        String taskId = "";
        if (bundle != null) {
            taskId = bundle.getString(BARG_TASK_ID);
        }
        return taskId;
    }

    private String getTaskName() {
        Bundle bundle = getArguments();
        String taskName = "";
        if (bundle != null) {
            taskName = bundle.getString(BARG_TASK_NAME);
        }
        return taskName;
    }
}
