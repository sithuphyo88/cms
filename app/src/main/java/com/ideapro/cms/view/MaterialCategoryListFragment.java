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
import com.ideapro.cms.data.MaterialCategoryEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.Controller.SearchController;
import com.ideapro.cms.view.listAdapter.MaterialCategoryListAdapter;
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
public class MaterialCategoryListFragment extends Fragment implements SearchView.OnQueryTextListener {

    View view;
    List<MaterialCategoryEntity> list;
    MaterialCategoryListAdapter adapter;
    private DaoFactory daoFactory;
    // start 2016/09/09 add search listner
    private MenuItemCompat.OnActionExpandListener mOnActionExpandListener;
    SearchController msController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnActionExpandListener = (MenuItemCompat.OnActionExpandListener) context;
        msController = (SearchController) context;
    }
    // end 2016/09/09 add search listner

    public MaterialCategoryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_material_category_list, container, false);
        setHasOptionsMenu(true);

        daoFactory = new DaoFactory(view.getContext());
        bindData();
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_material_category));

        // start 2016/09/09
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, mOnActionExpandListener);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setIconifiedByDefault(true); //iconify the widget
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        // end 2016/09/09

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        ImageButton imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);

        if (CommonUtils.CurrentUser.role.equals("admin")) {
            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new MaterialCategoryAddFragment());
                }
            });
        } else {
            imgAdd.setVisibility(View.GONE);
        }
    }

    private void bindData() {
        try {
            list = new ArrayList<>();
            // Dummy Data
            /*for (int i = 0; i < 5; i++) {
                MaterialCategoryEntity entity = new MaterialCategoryEntity();
                entity.name = "Material Category " + (i + 1);
                entity.description = "Material Category XXXXXXXXXXXX";

                list.add(entity);
            }*/
            Dao<MaterialCategoryEntity, String> materialCategoryEntityDao = daoFactory.getMaterialCategoryEntityDao();
            list = materialCategoryEntityDao.queryForAll();


            adapter = new MaterialCategoryListAdapter(view.getContext(), getActivity(), list);

            final SwipeMenuListView listView = (SwipeMenuListView) view.findViewById(R.id.listView);
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
                            MaterialCategoryAddFragment fragment = MaterialCategoryAddFragment.newInstance(list.get(position).id);
                            CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), fragment);
                            break;
                        case 1:
                            // delete
                            CommonUtils.showConfirmDialogBox(getActivity(), getString(R.string.message_confirmation_delete),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // START delete statment SNT 2016/09/09
                                            try {
                                                Dao<MaterialCategoryEntity, String> materialCategoryDao = daoFactory.getMaterialCategoryEntityDao();
                                                int i = materialCategoryDao.delete(list.get(position));
                                            } catch (SQLException e) {
                                                new Error(e.getMessage());
                                            }
                                            // END delete statment SNT 2016/09/09
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
                    MaterialCategoryAddFragment fragment = MaterialCategoryAddFragment.newInstance(list.get(position).id);
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

    // START Added search feature SNT 2016/09/09
    @Override
    public boolean onQueryTextSubmit(String query) {
        Dao<MaterialCategoryEntity, String> materialCategoryDao = null;
        try {
            QueryBuilder<MaterialCategoryEntity, String> qb = daoFactory.getMaterialCategoryEntityDao().queryBuilder();
            qb.where().like(MaterialCategoryEntity.CATEGORY_NAME, "%" + query.toString() + "%");

            PreparedQuery<MaterialCategoryEntity> pq = qb.prepare();
            list = daoFactory.getMaterialCategoryEntityDao().query(pq);

        } catch (SQLException e) {
            throw new Error(e);
        }

        // configure with adapter with data.
        adapter = new MaterialCategoryListAdapter(view.getContext(), getActivity(), list);

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





    // END Added search feature SNT 2016/09/09

}
