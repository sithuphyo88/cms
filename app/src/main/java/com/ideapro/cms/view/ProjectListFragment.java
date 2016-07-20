package com.ideapro.cms.view;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.ProjectEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.Controller.ProjectController;
import com.ideapro.cms.view.listAdapter.ProjectListAdapter;
import com.ideapro.cms.view.swipeMenu.SwipeMenu;
import com.ideapro.cms.view.swipeMenu.SwipeMenuCreator;
import com.ideapro.cms.view.swipeMenu.SwipeMenuItem;
import com.ideapro.cms.view.swipeMenu.SwipeMenuListView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectListFragment extends Fragment implements SearchView.OnQueryTextListener {

    View view;
    List<ProjectEntity> list;
    ProjectListAdapter adapter;
    private DaoFactory daoFactory;
    ProjectController mController;
    // start 2016/07/19 add search listner
    private MenuItemCompat.OnActionExpandListener mOnActionExpandListener;


    // end 2016/07/19
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnActionExpandListener = (MenuItemCompat.OnActionExpandListener) context;
        mController = (ProjectController) context;
    }


    public ProjectListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_project_list, container, false);
        setHasOptionsMenu(true);

        daoFactory = new DaoFactory(view.getContext());
        bindData();
        initializeUI();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_project));

        // start 2016/07/19
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, mOnActionExpandListener);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setIconifiedByDefault(true); //iconify the widget
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        // end 2016/07/19

    }

    private void initializeUI() {
        ImageButton imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);

        if (CommonUtils.CurrentUser.role.equals("admin")) {
            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*ProjectEntity entity = new ProjectEntity();
                    entity.name = "New Project";
                    entity.progress = "0";*/
                    ProjectAddFragment fragment = ProjectAddFragment.newInstance("");
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), fragment);
                }
            });
        } else {
            imgAdd.setVisibility(View.GONE);
        }
    }

    private void bindData() {
        try {
            list = new ArrayList<>();

            /*for (int i = 0; i < 100; i++) {
                ProjectEntity entity = new ProjectEntity();
                entity.name = "Project" + (i + 1);
                entity.progress = String.valueOf(i + 1);
                entity.startDate = "2016-05-" + (i + 1);
                entity.endDate = "2016-05-" + (i + 1);

                list.add(entity);
            }*/
            // get data from the database

            final Dao<ProjectEntity, String> projectEntityDao = daoFactory.getProjectEntityDao();
            list = projectEntityDao.queryForAll();

            adapter = new ProjectListAdapter(view.getContext(), getActivity(), list);

            SwipeMenuListView listView = (SwipeMenuListView) view.findViewById(R.id.listView);
            ColorDrawable myColor = new ColorDrawable(
                    this.getResources().getColor(R.color.color_accent)
            );
            listView.setDivider(myColor);
            listView.setDividerHeight(1);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    SwipeMenuItem deleteItem = new SwipeMenuItem(view.getContext());
                    deleteItem.setWidth(dp2px(50));
                    deleteItem.setTitle("Delete");
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
                            // delete
                            CommonUtils.showConfirmDialogBox(getActivity(), getString(R.string.message_confirmation_delete),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            // delete data from the table
                                            try {
                                                projectEntityDao.deleteById(list.get(position).id);
                                                list.remove(position);
                                            } catch (SQLException e) {
                                                throw new Error(e);
                                            }

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

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ProjectAddFragment fragment = ProjectAddFragment.newInstance(list.get(position).id);
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), fragment);
                }
            });

        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // start 2016/07/20
        Dao<ProjectEntity, String> projectEntityDao = null;
        try {
            QueryBuilder<ProjectEntity, String> qb = daoFactory.getProjectEntityDao().queryBuilder();
            qb.where().like(ProjectEntity.PROJECT_NAME, "%" + query.toString() + "%");
            PreparedQuery<ProjectEntity> pq = qb.prepare();
            list = daoFactory.getProjectEntityDao().query(pq);
        } catch (SQLException e) {
            throw new Error(e);
        }

        adapter = new ProjectListAdapter(view.getContext(), getActivity(), list);

        SwipeMenuListView listView = (SwipeMenuListView) view.findViewById(R.id.listView);
        ColorDrawable myColor = new ColorDrawable(
                this.getResources().getColor(R.color.color_accent)
        );
        listView.setDivider(myColor);
        listView.setDividerHeight(1);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (list.size() != 0) {
            mController.OnFound("project");
        }else{
            mController.OnNoFound("project");
        }
        // end 2016/07/20
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
