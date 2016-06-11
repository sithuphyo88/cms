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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.data.UserEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.EngineerListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EngineerListFragment extends Fragment {

    View view;
    ProjectEntity projectEntity;
    SiteEntity siteEntity;
    List<UserEntity> list;
    List<Boolean> selectedList;
    EngineerListAdapter adapter;
    ImageButton imgAdd;
    Menu menu;

    public EngineerListFragment() {
        this.siteEntity = new SiteEntity();
    }

    public EngineerListFragment(ProjectEntity projectEntity, SiteEntity siteEntity) {
        if(siteEntity == null) {
            this.siteEntity = new SiteEntity();
            this.projectEntity = new ProjectEntity();
        } else {
            this.siteEntity = siteEntity;
            this.projectEntity = projectEntity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_engineer_list, container, false);
        setHasOptionsMenu(true);
        bindData();
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_engineer_assign) + " for " + this.siteEntity.name);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void initializeUI() {
        imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteAddFragment(projectEntity, siteEntity));
            }
        });

        imgAdd.setVisibility(View.GONE);
    }

    private void bindData() {
        try {
            list = new ArrayList<>();
            selectedList = new ArrayList<Boolean>();

            for (int i = 0; i < 30; i++) {
                UserEntity entity = new UserEntity();
                entity.name = "Engineer " + (i + 1);
                list.add(entity);
                selectedList.add(false);
            }

            adapter = new EngineerListAdapter(this, view.getContext(), getActivity(), list);
            ListView listView = (ListView)view.findViewById(R.id.listView);
            ColorDrawable myColor = new ColorDrawable(
                    this.getResources().getColor(R.color.color_accent)
            );
            listView.setDivider(myColor);
            listView.setDividerHeight(1);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View innerView, int position, long id) {
                    CheckBox chkEngineerName = (CheckBox)innerView.findViewById(position);
                    chkEngineerName.setChecked(!chkEngineerName.isChecked());
                }
            });

        } catch (Exception e){
            throw new Error(e);
        }
    }

    public void selectCheckBox(View v) {
        CheckBox cb = (CheckBox) v;
        selectedList.set(cb.getId(), cb.isChecked());

        if(hasSelectedEngineer()) {
            imgAdd.setVisibility(View.VISIBLE);
        } else {
            imgAdd.setVisibility(View.GONE);
        }
    }

    private boolean hasSelectedEngineer() {
        for (int i = 0; i < this.selectedList.size(); i++) {
            if(this.selectedList.get(i)) {
                return true;
            }
        }

        return false;
    }
}
