package com.ideapro.cms.view;

/**
 * Created by sithu on 4/30/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ideapro.cms.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    private View view;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_about, container, false);
        setActionBar();
        return  view;
    }

    private void setActionBar() {
        getActivity().setTitle(getString(R.string.label_about));
    }
}
