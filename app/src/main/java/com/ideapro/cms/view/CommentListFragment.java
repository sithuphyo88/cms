package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.CommentEntity;
import com.ideapro.cms.view.listAdapter.CommentListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentListFragment extends Fragment {

    View view;
    List<CommentEntity> list;
    CommentListAdapter adapter;
    Menu menu;

    public CommentListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        setHasOptionsMenu(true);
        bindData();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_comments));
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void bindData() {
        try {
            list = new ArrayList<>();
            CommentEntity entity = new CommentEntity();
            entity.userName = "Si Thu Phyo (Engineer)";
            entity.commentDescription = "That design is so great!";
            entity.dateTime = "2016-06-05 1:00 PM";
            list.add(entity);

            entity = new CommentEntity();
            entity.userName = "Arkar Htet Myint (Designer)";
            entity.commentDescription = "Thank you.";
            entity.dateTime = "2016-06-05 1:10 PM";
            list.add(entity);

            entity = new CommentEntity();
            entity.userName = "Ye Htut Aung (Manager)";
            entity.commentDescription = "Could you please make Purchase Order for that design?";
            entity.dateTime = "2016-06-05 1:30 PM";
            list.add(entity);

            adapter = new CommentListAdapter(view.getContext(), getActivity(), list);
            ListView listView = (ListView) view.findViewById(R.id.listView);
            listView.setDivider(null);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
