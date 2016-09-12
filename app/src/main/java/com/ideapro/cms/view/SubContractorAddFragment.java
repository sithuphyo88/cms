package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.ideapro.cms.utils.CommonUtils;
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
        subContractor = new SubContractorEntity();

        if (bundle != null) {
            contractorId = bundle.getString(BARG_SUBCONTRACTOR_ID);
        }

        if (contractorId.isEmpty()) {
            reset();
        } else {
            try {
                flag_update = true;
                Dao<SubContractorEntity, String> subContractorDao = daoFactory.getSubContractorEntityDao();
                subContractor = subContractorDao.queryForId(contractorId);

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
        if (validation()) {
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
                Toast.makeText(getContext(), "Datas updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private boolean validation() {
        boolean flag = true;
        // Check user name
        if (TextUtils.isEmpty(etSubContractorName.getText().toString())) {
            etSubContractorName.setError(getString(R.string.error_missing_sub_contractor_name));
            flag = false;
        }

        // check phone number
        if (!TextUtils.isEmpty(etPhone.getText().toString())) {
            if (!CommonUtils.isValidPhoneNumber(etPhone.getText().toString())) {
                etPhone.setError(getString(R.string.error_wrong_format_phone_number));
                flag = false;
            }
        }

        // check email
        if (!TextUtils.isEmpty(etEmail.getText().toString())) {
            if (!CommonUtils.isEmailValid(etEmail.getText().toString())) {
                etEmail.setError(getString(R.string.error_wrong_email_format));
                flag = false;
            }
        }
        return flag;
    }

    private void saveData() {
        try {
            // This is how, a reference of DAO object can be done
            Dao<SubContractorEntity, String> subContractorDao = daoFactory.getSubContractorEntityDao();
            subContractor.subContracotr_id = UUID.randomUUID().toString();
            //This is the way to update data into a database table
            subContractorDao.create(subContractor);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void updateData() {
        try {
            // This is how, a reference of DAO object can be done
            Dao<SubContractorEntity, String> subContractorDao = daoFactory.getSubContractorEntityDao();

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
        subContractor.phone = etPhone.getText().toString();
    }
}
