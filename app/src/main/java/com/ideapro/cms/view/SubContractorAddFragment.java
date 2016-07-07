package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.CustomerEntity;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.SubContractorEntity;
import com.j256.ormlite.dao.Dao;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubContractorAddFragment extends Fragment {

    private static final String BARG_SUBCONTRACTOR_ID = "sub_contractor_id";
    View view;
    Menu menu;
    DaoFactory daoFactory;
    SubContractorEntity subContractor;
    private boolean flag_update;

    @BindView(R.id.txtSubContractorName)
    EditText etSubContractorName;

    @BindView(R.id.txtPhone)
    EditText etPhone;

    @BindView(R.id.txtEmail)
    EditText etEmail;

    @BindView(R.id.txtAddress)
    EditText etAddress;

    public static SubContractorAddFragment newInstance(String subContractorId) {

        Bundle args = new Bundle();
        args.putString(BARG_SUBCONTRACTOR_ID, subContractorId);
        SubContractorAddFragment fragment = new SubContractorAddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SubContractorAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sub_contractor_add, container, false);
        setHasOptionsMenu(true);

        // third party for find view by id
        ButterKnife.bind(this, view);
        daoFactory = new DaoFactory(view.getContext());

        initializeUI();

        return view;
    }

    private void initializeUI() {

        Bundle bundle = getArguments();
        String contractorId = "";

        if (bundle != null) {
            contractorId = bundle.getString(BARG_SUBCONTRACTOR_ID);
        }

        if (contractorId.isEmpty()) {
            reset();
        } else {
            try {
                flag_update = true;
                Dao<SubContractorEntity, UUID> subContractorDao = daoFactory.getSubContractorEntityDao();
                subContractor = subContractorDao.queryForId(UUID.fromString(contractorId));

                etSubContractorName.setText(subContractor.name);
                etPhone.setText(subContractor.phone);
                etEmail.setText(subContractor.email);
                etAddress.setText(subContractor.address);

            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    private void reset() {
        etSubContractorName.setText("");
        etPhone.setText("");
        etEmail.setText("");
        etAddress.setText("");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_sub_contractor));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            getData();
            if (flag_update) {
                updateData();
            } else {
                saveData();
            }
            reset();
        } catch (Exception e) {
            throw new Error(e);
        }
        if (!flag_update) {
            Toast.makeText(getContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private void saveData() {
        try {
            // This is how, a reference of DAO object can be done
            Dao<SubContractorEntity, UUID> subContractorDao = daoFactory.getSubContractorEntityDao();

            //This is the way to update data into a database table
            subContractorDao.create(subContractor);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void updateData() {
        try {
            // This is how, a reference of DAO object can be done
            Dao<SubContractorEntity, UUID> subContractorDao = daoFactory.getSubContractorEntityDao();

            //This is the way to update data into a database table
            subContractorDao.update(subContractor);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void getData() {
        subContractor.name = etSubContractorName.getText().toString();
        subContractor.address = etAddress.getText().toString();
        subContractor.email = etEmail.getText().toString();
        subContractor.phone = etEmail.getText().toString();
    }
}
