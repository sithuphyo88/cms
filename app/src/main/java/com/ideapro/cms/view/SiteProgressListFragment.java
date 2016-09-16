package com.ideapro.cms.view;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.data.SiteProgressHistoryEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.SiteProgressHistoryListAdapter;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SiteProgressListFragment extends Fragment {


    private static final String BARG_SITE_ID = "site_id";
    private static final String BARG_TASK_ID = "task_id";
    private static final String BARG_TASK_TITLE = "task_title";
    private static final String BARG_PROJECT_ID = "project_id";

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    List<SiteProgressHistoryEntity> list;
    SiteProgressHistoryListAdapter adapter;
    ImageButton imgAdd;

    private DaoFactory daoFactory;


    public SiteProgressListFragment() {
        this.projectEntity = new ProjectEntity();
        this.siteEntity = new SiteEntity();
    }

    public SiteProgressListFragment(ProjectEntity projectEntity, SiteEntity siteEntity) {
        if (projectEntity == null) {
            this.projectEntity = new ProjectEntity();
            this.siteEntity = new SiteEntity();
        } else {
            this.projectEntity = projectEntity;
            this.siteEntity = siteEntity;
        }
    }

    public static SiteProgressListFragment newInstance(String projectId, String siteId, String taskId, String taskTitle) {

        Bundle args = new Bundle();
        args.putString(BARG_PROJECT_ID, projectId);
        args.putString(BARG_SITE_ID, siteId);
        args.putString(BARG_TASK_ID, taskId);
        args.putString(BARG_TASK_TITLE, taskTitle);

        SiteProgressListFragment fragment = new SiteProgressListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_site_progress_list, container, false);
        setHasOptionsMenu(true);
        daoFactory = new DaoFactory(view.getContext());

        bindData();
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_progress) + " of " + getTaskTitle());
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteProgressAddFragment fragment = SiteProgressAddFragment.newInstance(getProjectId(), getSiteId(), getTaskId(), getTaskTitle());
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteProgressAddFragment(projectEntity, siteEntity));
            }
        });
    }

    private void bindData() {
        try {
            list = new ArrayList<>();

            final Dao<SiteProgressHistoryEntity, String> siteProgressHistoryDao = daoFactory.getSiteProgressHistoryDao();
            list = siteProgressHistoryDao.queryForEq(SiteProgressHistoryEntity.TASK_ID, getTaskId());

            adapter = new SiteProgressHistoryListAdapter(view.getContext(), getActivity(), list);
            ListView listView = (ListView) view.findViewById(R.id.listView);
            ColorDrawable myColor = new ColorDrawable(
                    this.getResources().getColor(R.color.color_accent)
            );
            listView.setDivider(myColor);
            listView.setDividerHeight(1);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteProgressEvidenceListFragment(list.get(position)));
                }
            });

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

    private String getTaskTitle() {
        Bundle bundle = getArguments();
        String taskTitle = "";
        if (bundle != null) {
            taskTitle = bundle.getString(BARG_TASK_TITLE);
        }
        return taskTitle;
    }

}

