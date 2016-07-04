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
import com.ideapro.cms.data.TaskEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.SiteTaskListAdapter;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SiteTaskListFragment extends Fragment {

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    List<TaskEntity> list;
    SiteTaskListAdapter adapter;
    ImageButton imgAdd;
    private DaoFactory daoFactory;

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

        daoFactory =  new DaoFactory(view.getContext());
        bindData();
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_task_list));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskEntity taskEntity = new TaskEntity();
                taskEntity.title = "New Task";
                taskEntity.description = "";
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteTaskAddFragment(projectEntity, siteEntity,taskEntity));
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

            Dao<TaskEntity,String> taskEntityDao =  daoFactory.getTaskEntityDao();
            list = taskEntityDao.queryBuilder().where().eq(TaskEntity.SITE_ID,this.siteEntity.id).query();

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
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteTaskAddFragment(projectEntity, siteEntity,list.get(position)));
                }
            });

        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
