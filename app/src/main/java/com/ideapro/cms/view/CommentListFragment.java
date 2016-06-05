package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.CommentEntity;
import com.ideapro.cms.data.SiteEntity;
import com.ideapro.cms.utils.CommonUtils;
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

    public CommentListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        bindData();
        setActionBar();
        // initializeUI();
        return view;
    }

    private void setActionBar() {
        CommonUtils.setActionBarForFragment((ActionBarActivity) getActivity(),
                getString(R.string.label_comments),
                R.mipmap.ic_search);
    }

    private void initializeUI() {
        ImageButton imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);

        if (CommonUtils.CurrentUser.role.equals("admin")) {
            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SiteEntity entity = new SiteEntity();
                    entity.name = "New Site";
                    entity.progress = "0";
                    //CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SiteAddFragment(projectEntity, entity));
                }
            });
        } else {
            imgAdd.setVisibility(View.GONE);
        }
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
