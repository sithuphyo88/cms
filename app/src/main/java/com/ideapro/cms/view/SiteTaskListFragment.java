package com.ideapro.cms.view;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.data.TaskEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.Controller.ProjectController;
import com.ideapro.cms.view.listAdapter.SiteListAdapter;
import com.ideapro.cms.view.listAdapter.SiteTaskListAdapter;
import com.ideapro.cms.view.swipeMenu.SwipeMenuListView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SiteTaskListFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String BARG_SITE_NAME = "site_name";
    private static final String BARG_SITE_ID = "site_id";
    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    List<TaskEntity> list;
    SiteTaskListAdapter adapter;
    ImageButton imgAdd;
    private DaoFactory daoFactory;

    ProjectController mController;
    // start 2016/07/25 add search listner
    private MenuItemCompat.OnActionExpandListener mOnActionExpandListener;
    // end 2016/07/25

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnActionExpandListener = (MenuItemCompat.OnActionExpandListener) context;
        mController = (ProjectController) context;
    }

    public static SiteTaskListFragment newInstance(String siteId, String siteName) {

        Bundle args = new Bundle();
        args.putString(BARG_SITE_ID, siteId);
        args.putString(BARG_SITE_NAME, siteName);
        SiteTaskListFragment fragment = new SiteTaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SiteTaskListFragment() {
        this.projectEntity = new ProjectEntity();
        this.siteEntity = new SiteEntity();
    }

    public SiteTaskListFragment(ProjectEntity projectEntity, SiteEntity siteEntity) {
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
        view = inflater.inflate(R.layout.fragment_site_task_list, container, false);
        setHasOptionsMenu(true);

        daoFactory = new DaoFactory(view.getContext());
        bindData();
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_task_list));
        super.onCreateOptionsMenu(menu, inflater);

        // start 2016/07/25
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, mOnActionExpandListener);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setIconifiedByDefault(true); //iconify the widget
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        // end 2016/07/25

    }

    private void initializeUI() {
        imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskEntity taskEntity = new TaskEntity();
                taskEntity.title = "New Task";
                taskEntity.description = "";

                SiteTaskAddFragment fragment = SiteTaskAddFragment.newInstance(getSiteId(), "", getSiteName());
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), fragment);
            }
        });
    }

    private void bindData() {
        try {
            list = new ArrayList<>();

            // dummy data
            /*for (int i = 0; i < 10; i++) {
                TaskEntity entity = new TaskEntity();
                entity.title = "Task " + (i + 1);
                entity.description = "Description " + (i + 1);
                entity.startDate = "2016-06-" + (i + 1);
                entity.endDate = "2016-06-" + (i + 1);
                entity.assignee = "Assignee " + (i + 1);

                list.add(entity);
            }*/

            // data form database

            Dao<TaskEntity, String> taskEntityDao = daoFactory.getTaskEntityDao();
            list = taskEntityDao.queryBuilder().where().eq(TaskEntity.SITE_ID, getSiteId()).query();

            adapter = new SiteTaskListAdapter(view.getContext(), getActivity(), list);
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
                    SiteTaskAddFragment fragment = SiteTaskAddFragment.newInstance(getSiteId(), list.get(position).id, getSiteName());
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), fragment);
                }
            });

        } catch (Exception e) {
            throw new Error(e);
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

    private String getSiteName() {
        Bundle bundle = getArguments();
        String siteName = "";
        if (bundle != null) {
            siteName = bundle.getString(BARG_SITE_NAME);
        }
        return siteName;
    }

    // 2016/07/25
    @Override
    public boolean onQueryTextSubmit(String taskName) {
        int condCount = 0;
        try {
            QueryBuilder<TaskEntity, String> qb = daoFactory.getTaskEntityDao().queryBuilder();
            Where<TaskEntity, String> where = qb.where();
            if (!getSiteId().isEmpty()) {
                where.eq(TaskEntity.SITE_ID, getSiteId());
                condCount++;
            }
            if (taskName.toString() != null) {
                where.like(TaskEntity.TITLE, "%" + taskName.toString() + "%");
                condCount++;
            }
            if (condCount > 0) {
                where.and(condCount);
            }
            // do the query
            list = qb.query();
        } catch (SQLException e) {
            throw new Error(e);
        }

        adapter = new SiteTaskListAdapter(view.getContext(), getActivity(), list);

        adapter = new SiteTaskListAdapter(view.getContext(), getActivity(), list);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        ColorDrawable myColor = new ColorDrawable(
                this.getResources().getColor(R.color.color_accent)
        );
        listView.setDivider(myColor);
        listView.setDividerHeight(1);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (list.size() != 0) {
            mController.OnFound("task");
        } else {
            mController.OnNoFound("task");
        }
        // end 2016/07/25

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
