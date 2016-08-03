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
import com.ideapro.cms.data.CustomerEntity;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.SubContractorCashFlowEntity;
import com.ideapro.cms.data.SubContractorEntity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubContractorCashFlowAddFragment extends Fragment {


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
        if (subContractorCashFlowEntity != null) {
            flag_update = true;
            setData();
        } else {
            reset();
        }
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
        etCreditAmount.setText("");
        etPaidAmount.setText("");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_cash_flow));
        super.onCreateOptionsMenu(menu, inflater);
    }
}
