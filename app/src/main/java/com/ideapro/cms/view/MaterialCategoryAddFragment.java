package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ideapro.cms.R;
import com.ideapro.cms.utils.CommonUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialCategoryAddFragment extends Fragment {

    View view;
    Menu menu;

    public MaterialCategoryAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_material_category_add, container, false);
        setHasOptionsMenu(true);

        Button butMaterials = (Button) view.findViewById(R.id.butMaterials);
        butMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new MaterialListFragment());
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_material_category));
        super.onCreateOptionsMenu(menu, inflater);
    }
}
