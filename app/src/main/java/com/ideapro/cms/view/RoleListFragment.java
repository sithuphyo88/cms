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

import com.ideapro.cms.R;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.RoleEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.Controller.SearchController;
import com.ideapro.cms.view.listAdapter.RoleListAdapter;
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
public class RoleListFragment extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    View view;
    List<RoleEntity> list;
    RoleListAdapter adapter;
    private DaoFactory daoFactory;

    // start 2016/09/10 add search listner
    private MenuItemCompat.OnActionExpandListener mOnActionExpandListener;
    SearchController msController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnActionExpandListener = (MenuItemCompat.OnActionExpandListener) context;
        msController = (SearchController) context;
    }
    // end 2016/09/10 add search listner


    public RoleListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_role_list, container, false);
        setHasOptionsMenu(true);

        //
        daoFactory = new DaoFactory(view.getContext());
        bindData();
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_role));

        // start 2016/09/10
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, mOnActionExpandListener);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setIconifiedByDefault(true); //iconify the widget
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        // end 2016/09/10
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        ImageButton imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);

        if (CommonUtils.CurrentUser.role.equals("admin")) {
            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RoleAddFragment fragment = RoleAddFragment.newInstance("");
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), fragment);
                }
            });
        } else {
            imgAdd.setVisibility(View.GONE);
        }
    }

    private void bindData() {
        try {
            // dummy data
            list = new ArrayList<>();
            /*for (int i = 0; i < 5; i++) {
                RoleEntity entity = new RoleEntity();
                entity.name = "Role " + (i + 1);
                entity.description = "XXXXXXXXXX";

                list.add(entity);
            }*/
            Dao<RoleEntity, String> roleEntityDao = daoFactory.getRoleEntityDao();
            list = roleEntityDao.queryForAll();

            adapter = new RoleListAdapter(view.getContext(), getActivity(), list);

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
                            RoleAddFragment fragment = RoleAddFragment.newInstance(list.get(position).id);
                            CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), fragment);
                            break;
                        case 1:
                            // delete
                            CommonUtils.showConfirmDialogBox(getActivity(), getString(R.string.message_confirmation_delete),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // START delete statment yha 2016/09/10
                                            try {
                                                Dao<RoleEntity, String> roleDao = daoFactory.getRoleEntityDao();
                                                int i = roleDao.delete(list.get(position));
                                            } catch (SQLException e) {
                                                new Error(e.getMessage());
                                            }
                                            // END delete statment yha 2016/09/10
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

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RoleAddFragment fragment = RoleAddFragment.newInstance(list.get(position).id);
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
    public boolean onClose() {
        initializeUI();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Dao<RoleEntity, String> roleDao = null;
        try {
            QueryBuilder<RoleEntity, String> qb = daoFactory.getRoleEntityDao().queryBuilder();
            qb.where().like(RoleEntity.NAME, "%" + query.toString() + "%");

            PreparedQuery<RoleEntity> pq = qb.prepare();
            list = daoFactory.getRoleEntityDao().query(pq);

        } catch (SQLException e) {
            throw new Error(e);
        }

        // configure with adapter with data.
        adapter = new RoleListAdapter(view.getContext(), getActivity(), list);

        // Swipe menu
        SwipeMenuListView listView = (SwipeMenuListView) view.findViewById(R.id.listView);
        ColorDrawable myColor = new ColorDrawable(
                this.getResources().getColor(R.color.color_accent)
        );
        listView.setDivider(myColor);
        listView.setDividerHeight(1);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        if (list.size() != 0) {
            msController.OnFound();
        } else {
            msController.OnNoFound();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
