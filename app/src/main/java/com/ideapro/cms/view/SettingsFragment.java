package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.AppSettingsEntity;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.utils.CommonUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

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

        daoFactory = new DaoFactory(view.getContext());
        setActionBar();
        initialize();

        return view;
    }

    private void setActionBar() {
        View customView = CommonUtils.setActionBarForFragment((ActionBarActivity)getActivity(),
                getString(R.string.label_settings),
                R.mipmap.ic_done);

        ImageButton imgButDone = (ImageButton) customView.findViewById(R.id.imgRight);
        imgButDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
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

    private String validInputValue(String serviceLink) {
        if(TextUtils.isEmpty(serviceLink)) {
            return getString(R.string.message_service_link_required);
        }

        return "";
    }

    private void save() {
        try {
            final AppSettingsEntity entity = new AppSettingsEntity();

            entity.key = AppSettingsEntity.SERVICE_LINK;
            entity.value = ((EditText) view.findViewById(R.id.txtServiceLink)).getText().toString();

            String message = validInputValue(entity.value);

            if (message.length() == 0) {
                Dao<AppSettingsEntity, String> appSettingsDao = daoFactory.getAppSettingsDao();
                appSettingsDao.update(entity);

                CommonUtils.showMessage(view.getContext(), getString(R.string.message_settings_save_success), Toast.LENGTH_SHORT);
            } else {
                CommonUtils.showAlertDialogBox(getActivity(), message);
            }

        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
