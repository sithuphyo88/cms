package com.ideapro.cms.view;


import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.SiteListAdapter;
import com.ideapro.cms.view.swipeMenu.SwipeMenu;
import com.ideapro.cms.view.swipeMenu.SwipeMenuCreator;
import com.ideapro.cms.view.swipeMenu.SwipeMenuItem;
import com.ideapro.cms.view.swipeMenu.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class SiteListFragment extends Fragment {

    View view;
    ProjectEntity projectEntity;
    List<SiteEntity> list;
    SiteListAdapter adapter;

    public SiteListFragment() {
        this.projectEntity = new ProjectEntity();
    }

    public SiteListFragment(ProjectEntity projectEntity) {
        if(projectEntity == null) {
            this.projectEntity = new ProjectEntity();
        } else {
            this.projectEntity = projectEntity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_site_list, container, false);
        setHasOptionsMenu(true);

        bindData();
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_site) + " for " + this.projectEntity.name);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void initializeUI() {
        ImageButton imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);

        if(CommonUtils.CurrentUser.role.equals("admin")) {
            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SiteEntity entity = new SiteEntity();
                    entity.name = "New Site";
                    entity.progress = "0";
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteAddFragment(projectEntity, entity));
                }
            });
        } else {
            imgAdd.setVisibility(View.GONE);
        }
    }

    private void bindData() {
        try {
            list = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                SiteEntity entity = new SiteEntity();
                entity.name = "Site " + (i + 1);
                entity.address = "Address " + (i + 1);
                entity.progress = String.valueOf(i + 1);
                entity.startDate = "2016-05-" + (i + 1);
                entity.endDate = "2016-05-" + (i + 1);

                list.add(entity);
            }

            adapter = new SiteListAdapter(view.getContext(), getActivity(), list);
            SwipeMenuListView listView = (SwipeMenuListView)view.findViewById(R.id.listView);
            ColorDrawable myColor = new ColorDrawable(
                    this.getResources().getColor(R.color.color_accent)
            );
            listView.setDivider(myColor);
            listView.setDividerHeight(1);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            if(CommonUtils.CurrentUser.role.equals("admin")) {
                SwipeMenuCreator creator = new SwipeMenuCreator() {
                    @Override
                    public void create(SwipeMenu menu) {
                        SwipeMenuItem editItem = new SwipeMenuItem(view.getContext());
                        editItem.setWidth(dp2px(50));
                        editItem.setTitle("edit");
                        editItem.setIcon(R.drawable.ic_edit);
                        menu.addMenuItem(editItem);

                        SwipeMenuItem deleteItem = new SwipeMenuItem(view.getContext());
                        deleteItem.setWidth(dp2px(50));
                        editItem.setTitle("delete");
                        deleteItem.setIcon(R.drawable.ic_delete);
                        menu.addMenuItem(deleteItem);
                    }
                };

                listView.setMenuCreator(creator);
                listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

                listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                        switch (index) {
                            case 0:
                                // edit
                                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteAddFragment(projectEntity, list.get(position)));
                                break;
                            case 1:
                                // delete
                                CommonUtils.showConfirmDialogBox(getActivity(), getString(R.string.message_confirmation_delete),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                list.remove(position);
                                                adapter.notifyDataSetChanged();
                                            }
                                        },
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                return;
                                            }
                                        });
                                break;
                        }
                        // false : close the menu; true : not close the menu
                        return false;
                    }
                });
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteAddFragment(projectEntity, list.get(position)));
                    /*UserEntity userEntity = (UserEntity)parent.getItemAtPosition(position);
                    if(userEntity.userCode.equals("") == false) {
                        UserAddFragment userAddFragment = new UserAddFragment(userEntity);
                        CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), userAddFragment);
                    }*/
                }
            });

        } catch (Exception e){
            throw new Error(e);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
