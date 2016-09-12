package com.ideapro.cms.view;


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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.CustomerEntity;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.utils.CommonUtils;
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
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ProjectAddFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    private static String BARG_PROJECT_ID = "project_id";
    View view;
    Button butAssignEngineers;
    Button butPurchaseOrder;
    Button butSite;
    Button butSubContractor;
    ProjectEntity projectEntity;

    @BindView(R.id.txtSiteName)
    EditText txtSiteName;

    @BindView(R.id.txtStartDate)
    EditText txtStartDate;

    @BindView(R.id.txtEndDate)
    EditText txtEndDate;

    @BindView(R.id.proProgress)
    ProgressBar proProgress;

    @BindView(R.id.tvwProgressValue)
    TextView tvwProgressValue;
    Menu menu;
    Spinner spnCustomer;
    private boolean flag_update;
    DaoFactory daoFactory;
    private boolean flag_start_date_click;
    private boolean flag_end_date_click;
    private List<CustomerEntity> list;
    private CustomSpannerAdapter mAdapter;

    public ProjectAddFragment() {
        // Required empty public constructor
    }

    public static ProjectAddFragment newInstance(String projectId) {

        Bundle args = new Bundle();
        args.putString(BARG_PROJECT_ID, projectId);
        ProjectAddFragment fragment = new ProjectAddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ProjectAddFragment(ProjectEntity projectEntity) {
        this.projectEntity = projectEntity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_project_add, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        daoFactory = new DaoFactory(view.getContext());
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_project_entry));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        butSite = (Button) view.findViewById(R.id.butSite);
        butSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteListFragment(projectEntity));
            }
        });

        butAssignEngineers = (Button) view.findViewById(R.id.butAssignEngineers);
        butAssignEngineers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new EngineerListFragment(projectEntity));
            }
        });

        butPurchaseOrder = (Button) view.findViewById(R.id.butPurchaseOrder);
        butPurchaseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new PurchaseOrderListFragment(projectEntity));
            }
        });

        butSubContractor = (Button) view.findViewById(R.id.butSubContractor);
        butSubContractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SubContractorSelectedListFragment(projectEntity));
            }
        });

        Button butComment = (Button) view.findViewById(R.id.butComment);
        butComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new CommentListFragment());
            }
        });

        // 2016/07/18 get project data from table
        Bundle bundle = getArguments();
        String projectId = "";

        projectEntity = new ProjectEntity();
        if (bundle != null) {
            projectId = bundle.getString(BARG_PROJECT_ID);
        }
        if (projectId.isEmpty()) {

            reset();
        } else {
            try {
                flag_update = true;
                Dao<ProjectEntity, String> projectDao = daoFactory.getProjectEntityDao();
                projectEntity = projectDao.queryForId(projectId);
                txtSiteName.setText(this.projectEntity.name);
                txtStartDate.setText(this.projectEntity.startDate);
                txtEndDate.setText(this.projectEntity.endDate);
                proProgress.setProgress(Integer.parseInt(this.projectEntity.progress));
                tvwProgressValue.setText(this.projectEntity.progress + " %");
            } catch (SQLException e) {
                throw new Error(e);
            }
        }

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
        // 2016/07/18
        spnCustomer = (Spinner) view.findViewById(R.id.spnCustomer);

        // get the customer list from db
        Dao<CustomerEntity, String> customerEntityDao = null;
        try {
            customerEntityDao = daoFactory.getCustomerEntityDao();
            list = customerEntityDao.queryForAll();
        } catch (SQLException e) {
            new Error(e);
        }

        // for customer list
        List<String> customers = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            customers.add(list.get(i).name);
        }

        customers.add(getString(R.string.choose_customer));

        mAdapter = new CustomSpannerAdapter(view.getContext(), R.layout.spinner_textview, customers);
        mAdapter.setDropDownViewResource(R.layout.spinner_textview);
        spnCustomer.setAdapter(mAdapter);
        spnCustomer.setPrompt(getString(R.string.choose_customer));

        if (projectEntity.customerId == null) {
            spnCustomer.setSelection(mAdapter.getCount());
        } else {
            int index = -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id.equals(projectEntity.customerId)) {
                    index = i;
                    break;
                }
            }
            spnCustomer.setSelection(index);
        }
    }

    private void reset() {
        txtSiteName.setText("");
        txtStartDate.setText("");
        txtEndDate.setText("");
        proProgress.setProgress(0);
        tvwProgressValue.setText("0 %");
        if (list != null)
            spnCustomer.setSelection(mAdapter.getCount());
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
        if (TextUtils.isEmpty(txtSiteName.getText().toString())) {
            flag = false;
            txtSiteName.setError(getString(R.string.error_missing_project_name));
        }

        // check start date
        if (TextUtils.isEmpty(txtStartDate.getText().toString())) {
            flag = false;
            txtStartDate.setError(getString(R.string.error_missing_project_start_date));
        }

        // check end date
        if (TextUtils.isEmpty(txtEndDate.getText().toString())) {
            flag = false;
            txtEndDate.setError(getString(R.string.error_missing_project_end_date));
        }

        // check customer
        if (spnCustomer.getSelectedItemPosition() == mAdapter.getCount()) {
            flag = false;
            ((TextView) spnCustomer.getChildAt(0)).setError(getString(R.string.error_missing_project_customer));
        }

        return flag;
    }

    private void saveData() throws SQLException {
        Dao<ProjectEntity, String> projectDao = daoFactory.getProjectEntityDao();
        projectEntity.id = UUID.randomUUID().toString();
        projectDao.create(projectEntity);
    }

    private void updateData() throws SQLException {
        Dao<ProjectEntity, String> projectDao = daoFactory.getProjectEntityDao();
        projectDao.update(projectEntity);
    }

    private void getData() {
        projectEntity.name = txtSiteName.getText().toString();
        projectEntity.startDate = txtStartDate.getText().toString();
        projectEntity.endDate = txtEndDate.getText().toString();
        projectEntity.customerId = String.valueOf(list.get((Integer) spnCustomer.getSelectedItemPosition()).id);
        projectEntity.progress = String.valueOf(proProgress.getProgress());
    }

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
