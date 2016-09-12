package com.ideapro.cms.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.CustomerEntity;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.data.TaskEntity;
import com.ideapro.cms.data.UserEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.utils.cmsAppConstants;
import com.ideapro.cms.view.fragments.BaseFragment;
import com.ideapro.cms.view.listAdapter.CustomSpannerAdapter;
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
public class SiteTaskAddFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    private static final String BARG_SITE_ID = "site_id";
    private static final String BARG_TASK_ID = "task_id";
    private static final String BARG_SITE_NAME = "site_name";
    private static final String BARG_PROJECT_ID = "project_id";
    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    TaskEntity taskEntity;
    Button butEvidence;
    Button butBluePrint;
    Button butComment;

    @BindView(R.id.spnAssignee)
    Spinner spnAsignee;

    private DaoFactory daoFactory;

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

    private CustomSpannerAdapter mAdapter;
    private List<UserEntity> enigneerlist;
    private boolean flag_update;

    public SiteTaskAddFragment() {
        // Required empty public constructor
    }

    public static SiteTaskAddFragment newInstance(String project_id, String siteId, String taskId, String siteName) {

        Bundle args = new Bundle();
        args.putString(BARG_PROJECT_ID, project_id);
        args.putString(BARG_SITE_ID, siteId);
        args.putString(BARG_TASK_ID, taskId);
        args.putString(BARG_SITE_NAME, siteName);
        SiteTaskAddFragment fragment = new SiteTaskAddFragment();
        fragment.setArguments(args);
        return fragment;
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
        daoFactory = new DaoFactory(view.getContext());

        // thrid party library for findviewbyID
        ButterKnife.bind(this, view);
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_site_task_entry) + " for " + getSiteName());
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {

        if (!getTaskId().isEmpty()) {
            setData();
        } else {
            reset();
        }
        // engineer spinner setting
        engineerSpinnerSetting();

        // start 2016/07/23
        txtStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showStartDatePicker();
                }
            }
        });

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePicker();
            }
        });

        txtEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showEndDatePicker();
                }
            }
        });

        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePicker();
            }
        });

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

    private void engineerSpinnerSetting() {
        Dao<UserEntity, String> userDao = null;
        try {
            userDao = daoFactory.getUserEntityDao();
            enigneerlist = userDao.queryForEq(UserEntity.ROLE_ID, cmsAppConstants.ROLE_ENGINEER);
        } catch (SQLException e) {
            new Error(e.getMessage());
        }

        List<String> engineers = new ArrayList<>();
        for (int i = 0; i < enigneerlist.size(); i++) {
            engineers.add(enigneerlist.get(i).name);
        }
        engineers.add(getString(R.string.choose_engineer));

        mAdapter = new CustomSpannerAdapter(view.getContext(), R.layout.spinner_textview, engineers);
        mAdapter.setDropDownViewResource(R.layout.spinner_textview);

        spnAsignee.setAdapter(mAdapter);
        spnAsignee.setPrompt(getString(R.string.choose_engineer));

        if (taskEntity.assignee == null) {
            spnAsignee.setSelection(mAdapter.getCount());
        } else {
            int index = -1;
            for (int i = 0; i < enigneerlist.size(); i++) {
                if (enigneerlist.get(i).id.equals(taskEntity.assignee)) {
                    index = i;
                    break;
                }
            }
            spnAsignee.setSelection(index);
        }
    }

    private void reset() {
        // title
        txtTitle.setText("");
        // address
        txtDescritpion.setText("");
        // start Date
        txtStartDate.setText("");
        // end Date
        txtEndDate.setText("");
        // asssingee
        tvwAssignee.setText("");

        taskEntity = new TaskEntity();
    }

    private void setData() {

        flag_update = true;
        try {
            Dao<TaskEntity, String> taskEntityDao = daoFactory.getTaskEntityDao();
            taskEntity = taskEntityDao.queryForId(getTaskId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
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


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
        }
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

    private String getSiteName() {
        Bundle bundle = getArguments();
        String siteName = "";
        if (bundle != null) {
            siteName = bundle.getString(BARG_SITE_NAME);
        }
        return siteName;
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
                showDialogDelay(1000, getString(R.string.message_save_success));
            } else {
                showDialogDelay(1000, getString(R.string.message_update_success));
            }
        }
        return true;
    }

    private boolean validation() {
        boolean flag = true;

        // check name
        if (TextUtils.isEmpty(txtTitle.getText().toString())) {
            flag = false;
            txtTitle.setError(getString(R.string.error_missing_task_name));
        }

        // check start date
        if (TextUtils.isEmpty(txtStartDate.getText().toString())) {
            flag = false;
            txtStartDate.setError(getString(R.string.error_missing_task_start_date));
        }

        // check end date
        if (TextUtils.isEmpty(txtEndDate.getText().toString())) {
            flag = false;
            txtEndDate.setError(getString(R.string.error_missing_task_end_date));
        }

        // check engineer
        if (spnAsignee.getSelectedItemPosition() == mAdapter.getCount()) {
            flag = false;
            ((TextView) spnAsignee.getChildAt(0)).setError(getString(R.string.error_missing_task_asignee));
        }

        return flag;
    }

    private void saveData() {
        try {
            // This is how, a reference of DAO object can be done
            Dao<TaskEntity, String> taskDao = daoFactory.getTaskEntityDao();

            taskEntity.id = UUID.randomUUID().toString();
            taskEntity.site_id = getSiteId();
            taskEntity.project_id = getProjectId();

            //This is the way to insert data into a database table
            taskDao.create(taskEntity);
        } catch (Exception e) {
            throw new Error(e);
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

    private void updateData() {
        try {
            // This is how, a reference of DAO object can be done
            Dao<TaskEntity, String> taskDao = daoFactory.getTaskEntityDao();

            //This is the way to update data into a database table
            taskDao.update(taskEntity);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void getData() {
        // title
        taskEntity.title = txtTitle.getText().toString();
        // description
        taskEntity.description = txtDescritpion.getText().toString();
        // start Date
        taskEntity.startDate = txtStartDate.getText().toString();
        // end Date
        taskEntity.endDate = txtEndDate.getText().toString();
        // asssingee
        taskEntity.assignee = String.valueOf(enigneerlist.get((Integer) spnAsignee.getSelectedItemPosition()).id);

    }

    // start 2016/07/23
    private void showStartDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog thirdPartyDatePicker = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        thirdPartyDatePicker.show(getActivity().getFragmentManager(), "showStartDatePicker");

    }

    private void showEndDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog thirdPartyDatePicker = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        thirdPartyDatePicker.show(getActivity().getFragmentManager(), "showEndDatePicker");

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(getContext(), "Year : " + year + " Month : " + monthOfYear + " Day : " + dayOfMonth, Toast.LENGTH_SHORT).show();
        if (view.getTag().toString().equals("showStartDatePicker")) {
            txtStartDate.setText(year + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth));
        }
        if (view.getTag().toString().equals("showEndDatePicker")) {
            txtEndDate.setText(year + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth));
        }
    }


}
