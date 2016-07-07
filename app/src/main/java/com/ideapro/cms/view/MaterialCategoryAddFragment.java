package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.CustomerEntity;
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.MaterialCategoryEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialCategoryAddFragment extends Fragment {

    private static final String BARG_MATERIAL_CATEGORY_ID = "material_category_id";
    View view;
    Menu menu;
    DaoFactory daoFactory;
    private MaterialCategoryEntity materialCategory;
    private boolean flag_update;

    @BindView(R.id.txtMaterialCategoryName)
    EditText txtMaterialCategoryName;

    @BindView(R.id.txtDescription)
    EditText txtDescription;

    public MaterialCategoryAddFragment() {
        // Required empty public constructor
    }

    public static MaterialCategoryAddFragment newInstance(String materialCategoryId) {

        Bundle args = new Bundle();
        args.putString(BARG_MATERIAL_CATEGORY_ID, materialCategoryId);
        MaterialCategoryAddFragment fragment = new MaterialCategoryAddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_material_category_add, container, false);
        setHasOptionsMenu(true);

        // third party for find view by id
        ButterKnife.bind(this, view);
        daoFactory = new DaoFactory(view.getContext());

        initializeUI();
        Button butMaterials = (Button) view.findViewById(R.id.butMaterials);
        butMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialListFragment fragment = MaterialListFragment.newInstance(materialCategory.id);
                CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), fragment);
            }
        });

        return view;
    }

    private void initializeUI() {
        Bundle bundle = getArguments();
        String materialCategoryId = "";
        materialCategory = new MaterialCategoryEntity();

        if (bundle != null) {
            materialCategoryId = bundle.getString(BARG_MATERIAL_CATEGORY_ID);
        }

        if (materialCategoryId.isEmpty()) {
            reset();
        } else {
            try {
                flag_update = true;
                Dao<MaterialCategoryEntity, String> materialCategoryDao = daoFactory.getMaterialCategoryEntityDao();
                materialCategory = materialCategoryDao.queryForId(materialCategoryId);
                txtMaterialCategoryName.setText(materialCategory.name);
                txtDescription.setText(materialCategory.description);

            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    private void reset() {
        txtDescription.setText("");
        txtMaterialCategoryName.setText("");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_material_category));
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
        } catch (Exception e) {
            throw new Error(e);
        }
        if (!flag_update) {
            Toast.makeText(getContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
        }
        reset();
        return true;
    }

    private void saveData() throws SQLException {
        Dao<MaterialCategoryEntity, String> materialCategoryEntityDao = daoFactory.getMaterialCategoryEntityDao();
        materialCategory.id = UUID.randomUUID().toString();
        materialCategoryEntityDao.create(materialCategory);
    }

    private void updateData() throws SQLException {
        Dao<MaterialCategoryEntity, String> materialCategoryEntityDao = daoFactory.getMaterialCategoryEntityDao();
        materialCategoryEntityDao.update(materialCategory);
    }

    private void getData() {
        materialCategory.name = txtMaterialCategoryName.getText().toString();
        materialCategory.description = txtMaterialCategoryName.getText().toString();
    }
}
