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
import android.widget.ListView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.PermissionEntity;
import com.ideapro.cms.view.listAdapter.PermissionListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermissionListFragment extends Fragment {

    View view;
    List<PermissionEntity> list;
    PermissionListAdapter adapter;
    Menu menu;

    public PermissionListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_permission_list, container, false);
        setHasOptionsMenu(true);
        bindData();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_assign_permission));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void bindData() {
        try {
            list = new ArrayList<>();

            for (int i = 0; i < 30; i++) {
                PermissionEntity entity = new PermissionEntity();
                entity.title = "Permission " + (i + 1);
                entity.description = "XXXXXXXXXXXXXXXXXXXXXXXX";
                list.add(entity);
            }

            adapter = new PermissionListAdapter(view.getContext(), getActivity(), list);
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
                public void onItemClick(AdapterView<?> parent, View innerView, int position, long id) {
                    CheckBox chk = (CheckBox) innerView.findViewById(position);
                    chk.setChecked(!chk.isChecked());
                }
            });

        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
