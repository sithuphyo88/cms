package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.j256.ormlite.dao.Dao;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class SiteAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private static final String BARG_SITE_ID = "site_id"; //implements View.OnClickListener
    private static final String BARG_PROJECT_ID = "project_id";
    private static final String BARG_PROJECT_NAME = "project_name";

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;

    @BindView((R.id.butTaskList))
    Button butTaskList;


    @BindView((R.id.txtSiteName))
    EditText txtSiteName;
    @BindView(R.id.txtAddress)
    EditText txtAddress;
    @BindView(R.id.txtStartDate)
    EditText txtStartDate;
    @BindView(R.id.txtEndDate)
    EditText txtEndDate;
    @BindView(R.id.proProgress)
    ProgressBar proProgress;
    @BindView(R.id.tvwProgressValue)
    TextView tvwProgressValue;

    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private boolean flag_update;
    private DaoFactory daoFactory;


    public static SiteAddFragment newInstance(String siteId, String projectId, String projectName) {

        Bundle args = new Bundle();
        args.putString(BARG_SITE_ID, siteId);
        args.putString(BARG_PROJECT_ID, projectId);
        args.putString(BARG_PROJECT_NAME, projectName);
        SiteAddFragment fragment = new SiteAddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SiteAddFragment() {
        // Required empty public constructor

    }

    public SiteAddFragment(ProjectEntity projectEntity, SiteEntity siteEntity) {
        if (projectEntity == null) {
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
        ButterKnife.bind(this, view);


        daoFactory = new DaoFactory(view.getContext());

        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        Bundle bundle = getArguments();
        String projectName = "";
        if (bundle != null) {
            projectName = bundle.getString(BARG_PROJECT_NAME);
        }
        getActivity().setTitle(getString(R.string.label_site_entry) + " for " + projectName);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
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

        // 2016/07/23 get project data from table
        Bundle bundle = getArguments();
        String siteId = "";
        siteEntity = new SiteEntity();

        // get site id from the bundle passing by other screen
        if (bundle != null) {
            siteId = bundle.getString(BARG_SITE_ID);
        }

        // if site id empty just show the initial state
        if (siteId.isEmpty()) {
            reset();
            // If site id is not empty get the data from table by site id.
        } else {
            try {
                flag_update = true;
                Dao<SiteEntity, String> siteDao = daoFactory.getSiteEntityDao();
                siteEntity = siteDao.queryForId(siteId);
                txtSiteName.setText(this.siteEntity.name);
                txtAddress.setText(this.siteEntity.address);
                txtStartDate.setText(this.siteEntity.startDate);
                txtEndDate.setText(this.siteEntity.endDate);
                proProgress.setProgress(Integer.parseInt(this.siteEntity.progress));
                tvwProgressValue.setText(this.siteEntity.progress + " %");

            } catch (SQLException e) {
                throw new Error(e);
            }

        }

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
    }

    private void reset() {
        txtSiteName.setText("");
        txtStartDate.setText("");
        txtEndDate.setText("");
        txtAddress.setText("");
        proProgress.setProgress(0);
        tvwProgressValue.setText("0 %");

    }

    // end 2016/07/23 get project data from table

    // end 2016/07/23


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

        return true;
    }

    private void saveData() throws SQLException {
        Dao<SiteEntity, String> siteDao = daoFactory.getSiteEntityDao();
        siteEntity.id = UUID.randomUUID().toString();
        siteEntity.project_id = getProjectId();
        siteDao.create(siteEntity);
    }

    private String getProjectId() {
        Bundle bundle = getArguments();
        String projectId = "";
        if (bundle != null) {
            projectId = bundle.getString(BARG_PROJECT_ID);
        }

        return projectId;
    }

    private void updateData() throws SQLException {
        Dao<SiteEntity, String> siteDao = daoFactory.getSiteEntityDao();
        siteDao.update(siteEntity);

    }

    private void getData() {
        siteEntity.name = txtSiteName.getText().toString();
        siteEntity.startDate = txtStartDate.getText().toString();
        siteEntity.endDate = txtEndDate.getText().toString();
        siteEntity.address = txtAddress.getText().toString();
    }

    // end 2016/07/23
}
