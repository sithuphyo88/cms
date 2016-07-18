package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ProjectAddFragment extends Fragment {

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
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SubContractorSelectedListFragment());
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
        // 2016/07/18

        spnCustomer = (Spinner) view.findViewById(R.id.spnCustomer);

        //TODO change the dummy to table data
        List<String> customers = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            customers.add("Customer " + (i + 1));
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_textview,
                customers);
        mAdapter.setDropDownViewResource(R.layout.spinner_textview);
        spnCustomer.setAdapter(mAdapter);
    }

    private void reset() {
        txtSiteName.setText("");
        txtStartDate.setText("");
        txtEndDate.setText("");
        proProgress.setProgress(0);
        tvwProgressValue.setText("0 %");
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
        projectEntity.progress = String.valueOf(proProgress.getProgress());
    }
}
