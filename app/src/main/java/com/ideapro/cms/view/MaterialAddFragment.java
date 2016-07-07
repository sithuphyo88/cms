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
import com.ideapro.cms.data.DaoFactory;
import com.ideapro.cms.data.MaterialEntity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialAddFragment extends Fragment {

    private static final String BARG_MATERIAL_ID = "material_id";
    private static final String BARG_MATERIAL_CATEGORY_ID = "material_group_id";
    View view;
    Menu menu;

    @BindView(R.id.txtMaterialName)
    EditText txtMaterialName;

    @BindView(R.id.txtDescription)
    EditText txtDescription;

    private DaoFactory daoFactory;
    private boolean flag_update;
    private MaterialEntity material;
    private String materialGroupId;

    public MaterialAddFragment() {
        // Required empty public constructor
    }

    public static MaterialAddFragment newInstance(String materialId, String materialCategoryId) {

        Bundle args = new Bundle();
        args.putString(BARG_MATERIAL_ID, materialId);
        args.putString(BARG_MATERIAL_CATEGORY_ID, materialCategoryId);
        MaterialAddFragment fragment = new MaterialAddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_material_add, container, false);
        setHasOptionsMenu(true);

        daoFactory = new DaoFactory(view.getContext());
        ButterKnife.bind(this, view);

        initializeUI();

        return view;
    }

    private void initializeUI() {
        Bundle bundle = getArguments();
        String materialId = "";
        material = new MaterialEntity();
        if (bundle != null) {
            materialId = bundle.getString(BARG_MATERIAL_ID);
            materialGroupId = bundle.getString(BARG_MATERIAL_CATEGORY_ID);
        }
        if (materialId.isEmpty()) {
            reset();
        } else {
            try {
                flag_update = true;
                Dao<MaterialEntity, String> materialDao = daoFactory.getMaterialEntityDao();
                material = materialDao.queryForId(materialId);
                txtMaterialName.setText(material.name);
                txtDescription.setText(material.description);
            } catch (SQLException e) {
                throw new Error(e);
            }
        }
    }

    private void reset() {
        txtDescription.setText("");
        txtMaterialName.setText("");
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
        Dao<MaterialEntity, String> materialDao = daoFactory.getMaterialEntityDao();
        material.id = UUID.randomUUID().toString();
        material.materialCategoryId = materialGroupId;
        materialDao.create(material);
    }

    private void updateData() throws SQLException {
        Dao<MaterialEntity, String> materialDao = daoFactory.getMaterialEntityDao();
        materialDao.update(material);
    }

    private void getData() {
        material.name = txtMaterialName.getText().toString();
        material.description = txtDescription.getText().toString();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save, menu);
        getActivity().setTitle(getString(R.string.label_material_list));
        super.onCreateOptionsMenu(menu, inflater);
    }
}
