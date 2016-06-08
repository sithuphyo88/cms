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
import com.ideapro.cms.utils.CommonUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class ProjectAddFragment extends Fragment {

    View view;
    Button butDesigns;
    ProjectEntity projectEntity;
    EditText txtSiteName;
    EditText txtStartDate;
    EditText txtEndDate;
    ProgressBar proProgress;
    TextView tvwProgressValue;
    Menu menu;

    public ProjectAddFragment() {
        // Required empty public constructor
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

        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_project_entry));
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void initializeUI() {
        butDesigns = (Button) view.findViewById(R.id.butDesigns);
        butDesigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteListFragment(projectEntity));
            }
        });

        if(this.projectEntity != null) {
            txtSiteName = (EditText) view.findViewById(R.id.txtSiteName);
            txtSiteName.setText(this.projectEntity.name);

            txtStartDate = (EditText) view.findViewById(R.id.txtStartDate);
            txtStartDate.setText(this.projectEntity.startDate);

            txtEndDate = (EditText) view.findViewById(R.id.txtEndDate);
            txtEndDate.setText(this.projectEntity.endDate);

            proProgress = (ProgressBar) view.findViewById(R.id.proProgress);
            proProgress.setProgress(Integer.parseInt(this.projectEntity.progress));

            tvwProgressValue = (TextView) view.findViewById(R.id.tvwProgressValue);
            tvwProgressValue.setText(this.projectEntity.progress + " %");
        }
    }
}
