package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.data.SiteProgressHistoryEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.SiteProgressHistoryListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SiteProgressListFragment extends Fragment {

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    List<SiteProgressHistoryEntity> list;
    SiteProgressHistoryListAdapter adapter;
    ImageButton imgAdd;

    public SiteProgressListFragment() {
        this.projectEntity = new ProjectEntity();
        this.siteEntity = new SiteEntity();
    }

    public SiteProgressListFragment(ProjectEntity projectEntity, SiteEntity siteEntity) {
        if(projectEntity == null) {
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
        view = inflater.inflate(R.layout.fragment_site_progress_list, container, false);
        bindData();
        setActionBar();
        initializeUI();
        return view;
    }

    private void initializeUI() {
        imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);
        if(!CommonUtils.CurrentUser.role.equals("admin")) {
            imgAdd.setVisibility(View.VISIBLE);
            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteProgressAddFragment(projectEntity, siteEntity));
                }
            });
        }
    }

    private void setActionBar() {
        CommonUtils.setActionBarForFragment((ActionBarActivity)getActivity(),
                getString(R.string.label_progress) + " of " + this.siteEntity.name,
                R.mipmap.ic_search);
    }

    private void bindData() {
        try {
            list = new ArrayList<>();
            int size = Integer.parseInt(siteEntity.progress);
            for (int i = 0; i < size; i++) {
                SiteProgressHistoryEntity entity = new SiteProgressHistoryEntity();
                entity.siteName = siteEntity.name;
                entity.date = "2016-05- " + (i + 1);
                entity.engineerName = "Engineer - Mg Ba";
                entity.description = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXYYYYYYYYYYYYYYYYYY";
                entity.progress = String.valueOf(i + 1);

                list.add(entity);
            }

            adapter = new SiteProgressHistoryListAdapter(view.getContext(), getActivity(), list);
            ListView listView = (ListView)view.findViewById(R.id.listView);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteProgressEvidenceListFragment(list.get(position)));
                }
            });

        } catch (Exception e){
            throw new Error(e);
        }
    }
}
