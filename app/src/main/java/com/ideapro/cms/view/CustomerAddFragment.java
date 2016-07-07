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
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerAddFragment extends Fragment {

    public static final String BARG_CUSTOMER_ID = "customer_id";

    View view;
    Menu menu;

    @BindView(R.id.txtCustomerName)
    EditText txtCustomerName;

    @BindView((R.id.txtPhone))
    EditText txtPhone;

    @BindView(R.id.txtEmail)
    EditText txtEmail;

    @BindView(R.id.txtAddress)
    EditText txtAddress;

    DaoFactory daoFactory;

    CustomerEntity customer;
    private boolean flag_update;

    public CustomerAddFragment() {
        // Required empty public constructor
    }

    public static CustomerAddFragment newInstance(String customerId) {

        Bundle args = new Bundle();
        args.putString(BARG_CUSTOMER_ID, customerId);
        CustomerAddFragment fragment = new CustomerAddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer_add, container, false);
        setHasOptionsMenu(true);

        // third party for find view by id
        ButterKnife.bind(this, view);
        daoFactory = new DaoFactory(view.getContext());

        initializeUI();
        return view;
    }

    private void initializeUI() {
        Bundle bundle = getArguments();
        String customerId = "";
        if (bundle != null) {
            customerId = bundle.getString(BARG_CUSTOMER_ID);
        }
        if (customerId.isEmpty()) {
            reset();
        } else {
            try {
                flag_update = true;
                Dao<CustomerEntity, String> customerDao = daoFactory.getCustomerEntityDao();
                customer = customerDao.queryForId(customerId);
                txtCustomerName.setText(customer.name);
                txtPhone.setText(customer.phone);
                txtAddress.setText(customer.address);
                txtEmail.setText(customer.email);

            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_customer));
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

    private void updateData() {
        try {
            // This is how, a reference of DAO object can be done
            Dao<CustomerEntity, String> customerDao = daoFactory.getCustomerEntityDao();

            //This is the way to update data into a database table
            customerDao.update(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getData() {
        customer = new CustomerEntity();
        customer.name = txtCustomerName.getText().toString();
        customer.phone = txtPhone.getText().toString();
        customer.address = txtAddress.getText().toString();
        customer.email = txtAddress.getText().toString();
        customer.id = txtCustomerName.getText().toString();
    }

    private void saveData() throws Exception {
        try {
            // This is how, a reference of DAO object can be done
            Dao<CustomerEntity, String> customerDao = daoFactory.getCustomerEntityDao();

            //This is the way to insert data into a database table
            customerDao.create(customer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        txtCustomerName.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
    }
}
