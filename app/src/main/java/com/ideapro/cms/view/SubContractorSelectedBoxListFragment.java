package com.ideapro.cms.view;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.ideapro.cms.R;
import com.ideapro.cms.data.SubContractorEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.SubContractorSelectBoxListAdapter;
import com.ideapro.cms.view.swipeMenu.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubContractorSelectedBoxListFragment extends Fragment {

    View view;
    List<SubContractorEntity> list;
    SubContractorSelectBoxListAdapter adapter;
    List<Boolean> selectedList;
    Menu menu;
    ImageButton imgAdd;

    public SubContractorSelectedBoxListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sub_contractor_select_box_list, container, false);
        setHasOptionsMenu(true);

        bindData();
        initializeUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_choose_sub_contractor));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);
        imgAdd.setVisibility(View.GONE);

        if (CommonUtils.CurrentUser.role.equals("admin")) {
            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new SubContractorSelectedListFragment());
                }
            });
        } else {
            imgAdd.setVisibility(View.GONE);
        }
    }

    private void bindData() {
        try {
            selectedList = new ArrayList<Boolean>();
            list = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                SubContractorEntity entity = new SubContractorEntity();
                entity.name = "Sub-Contractor " + (i + 1);
                entity.phone = "0979516247" + (i + 1);
                entity.email = "Sub-Contractor" + (i + 1) + "@gmail.com";
                entity.address = "Yangon " + (i + 1);

                selectedList.add(false);
                list.add(entity);
            }

            adapter = new SubContractorSelectBoxListAdapter(this, view.getContext(), getActivity(), list);

            SwipeMenuListView listView = (SwipeMenuListView) view.findViewById(R.id.listView);
            ColorDrawable myColor = new ColorDrawable(
                    this.getResources().getColor(R.color.color_accent)
            );
            listView.setDivider(myColor);
            listView.setDividerHeight(1);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public void selectCheckBox(View v) {
        CheckBox cb = (CheckBox) v;
        selectedList.set(cb.getId(), cb.isChecked());

        if (hasSelectedItem()) {
            imgAdd.setVisibility(View.VISIBLE);
        } else {
            imgAdd.setVisibility(View.GONE);
        }
    }

    private boolean hasSelectedItem() {
        for (int i = 0; i < this.selectedList.size(); i++) {
            if (this.selectedList.get(i)) {
                return true;
            }
        }

        return false;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
