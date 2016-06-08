package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ideapro.cms.R;
import com.ideapro.cms.data.AppSettingsEntity;
import com.ideapro.cms.data.DaoFactory;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private View view;
    private DaoFactory daoFactory;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        setHasOptionsMenu(true);

        daoFactory = new DaoFactory(view.getContext());
        initialize();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_settings));
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void initialize() {
        try {
            Dao<AppSettingsEntity, String> appSettingsDao = daoFactory.getAppSettingsDao();
            AppSettingsEntity entity = appSettingsDao.queryForId(AppSettingsEntity.SERVICE_LINK);
            ((EditText) view.findViewById(R.id.txtServiceLink)).setText(entity.value);

        } catch (SQLException e) {
            throw new Error(e);
        }
    }
}
