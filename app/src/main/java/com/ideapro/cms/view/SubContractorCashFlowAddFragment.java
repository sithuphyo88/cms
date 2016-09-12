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
import com.ideapro.cms.data.SubContractorCashFlowEntity;
import com.ideapro.cms.data.SubContractorEntity;
import com.j256.ormlite.dao.Dao;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubContractorCashFlowAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener {


    View view;
    @BindView(R.id.txtDate)
    EditText etDate;

    @BindView(R.id.txtDescription)
    EditText etDescription;

    @BindView(R.id.txtCreditAmount)
    EditText etCreditAmount;

    @BindView(R.id.txtPaidAmount)
    EditText etPaidAmount;

    Menu menu;
    DaoFactory daoFactory;
    private boolean flag_update;
    SubContractorCashFlowEntity subContractorCashFlowEntity;


    public SubContractorCashFlowAddFragment() {
        // Required empty public constructor
    }

    public SubContractorCashFlowAddFragment(SubContractorCashFlowEntity subContractorCashFlowEntity) {
        this.subContractorCashFlowEntity = subContractorCashFlowEntity;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sub_contractor_cash_flow_add, container, false);
        setHasOptionsMenu(true);

        // third party for find view by id
        ButterKnife.bind(this, view);
        daoFactory = new DaoFactory(view.getContext());

        initializeUI();
        return view;
    }

    private void initializeUI() {
        if (subContractorCashFlowEntity.description != null) {
            flag_update = true;
            setData();
        } else {
            reset();
        }

        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();
                }
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

    }

    private void setData() {
        etDate.setText(subContractorCashFlowEntity.date);
        etDescription.setText(subContractorCashFlowEntity.description);
        etCreditAmount.setText(String.valueOf(subContractorCashFlowEntity.creditAmount));
        etPaidAmount.setText(String.valueOf(subContractorCashFlowEntity.paidAmount));
    }

    private void reset() {
        etDate.setText("");
        etDescription.setText("");
        etCreditAmount.setText("0");
        etPaidAmount.setText("0");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_cash_flow));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // START Added 2016/09/12 Sai Num Town
        if (validation()) {
            // END Added 2016/09/12 Sai Num Town
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
            // START Added 2016/09/12 Sai Num Town
        }
        // END Added 2016/09/12 Sai Num Town
        return true;
    }

    private void saveData() {
        try {
            // This is how, a reference of DAO object can be done
            Dao<SubContractorCashFlowEntity, String> subContractCashFlowDao = daoFactory.getSubContractorCashFlowEntityDao();

            subContractorCashFlowEntity.id = UUID.randomUUID().toString();

            //This is the way to insert data into a database table
            subContractCashFlowDao.create(subContractorCashFlowEntity);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void updateData() {
        try {
            // This is how, a reference of DAO object can be done
            Dao<SubContractorCashFlowEntity, String> subContractCashFlowDao = daoFactory.getSubContractorCashFlowEntityDao();

            //This is the way to update data into a database table
            subContractCashFlowDao.update(subContractorCashFlowEntity);
        } catch (Exception e) {
            throw new Error(e);
        }
    }


    private void getData() {

        subContractorCashFlowEntity.date = etDate.getText().toString();
        subContractorCashFlowEntity.description = etDescription.getText().toString();
        subContractorCashFlowEntity.creditAmount = Long.parseLong(String.valueOf(etCreditAmount.getText()));
        subContractorCashFlowEntity.paidAmount = Long.parseLong(String.valueOf(etPaidAmount.getText()));
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog thirdPartyDatePicker = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        thirdPartyDatePicker.show(getActivity().getFragmentManager(), "showDatePicker");

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(getContext(), "Year : " + year + " Month : " + monthOfYear + " Day : " + dayOfMonth, Toast.LENGTH_SHORT).show();
        if (view.getTag().toString().equals("showDatePicker")) {
            etDate.setText(year + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth));
        }

    }

    // START Added 2016/09/12 Sai Num Town
    private boolean validation() {
        boolean flag = false;
        if (TextUtils.isEmpty(etDate.getText().toString()) || TextUtils.isEmpty(etDescription.getText().toString().trim())
                || TextUtils.isEmpty((etCreditAmount.getText().toString())) || TextUtils.isEmpty((etPaidAmount.getText().toString()))) {
            flag = false;
            // One of the required data is empty
            if (TextUtils.isEmpty(etDate.getText().toString())) {
                etDate.setError(getString(R.string.error_missing_date));
            }
            if (TextUtils.isEmpty(etDescription.getText().toString().trim())) {
                etDescription.setError(getString(R.string.error_missing_description));
            }
            if (TextUtils.isEmpty(etCreditAmount.getText().toString())) {
                etCreditAmount.setError(getString(R.string.error_missing_credit_amount));
            }
            if (TextUtils.isEmpty(etPaidAmount.getText().toString())) {
                etPaidAmount.setError(getString(R.string.error_missing_paid_amount));
            }
        } else {
            flag = true;
        }
        return flag;
    }
    // END Added 2016/09/12 Sai Num Town
}
