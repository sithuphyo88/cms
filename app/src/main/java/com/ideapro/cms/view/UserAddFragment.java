package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ideapro.cms.R;
import com.ideapro.cms.view.listAdapter.RoleSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserAddFragment extends Fragment {

    View view;
    Menu menu;
    Spinner spnRole;

    public UserAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_add, container, false);
        setHasOptionsMenu(true);

        initializeUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_user));
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initializeUI() {
        spnRole = (Spinner) view.findViewById(R.id.spnRole);
        List<String> customers = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            customers.add("Role " + (i + 1));
        }

        ArrayAdapter<String> mAdapter = new RoleSpinnerAdapter(view.getContext(), getActivity(), customers);
        spnRole.setAdapter(mAdapter);
    }
}
