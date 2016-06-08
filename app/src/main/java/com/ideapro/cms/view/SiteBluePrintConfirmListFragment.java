package com.ideapro.cms.view;


import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ImageItem;
import com.ideapro.cms.data.SiteProgressHistoryEntity;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.SiteBluePrintConfirmListAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SiteBluePrintConfirmListFragment extends Fragment {

    View view;
    private GridView gridView;
    private SiteBluePrintConfirmListAdapter adapter;
    ArrayList<ImageItem> imageItems;
    SiteProgressHistoryEntity entity;
    ImageButton imgRight;
    Menu menu;

    public SiteBluePrintConfirmListFragment() {
    }

    public SiteBluePrintConfirmListFragment(SiteProgressHistoryEntity entity) {
        this.entity = entity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_site_progress_evidence_list, container, false);
        setHasOptionsMenu(true);

        bindData();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(entity.siteName + " - " + getString(R.string.label_blueprints));
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void bindData() {
        try {
            if(imageItems == null) {
                imageItems = getData();
            }

            gridView = (GridView) view.findViewById(R.id.gridView);

            adapter = new SiteBluePrintConfirmListAdapter(this, view.getContext(), R.layout.fragment_site_blueprint_confirm_list_item, imageItems);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e){
            throw new Error(e);
        }
    }

    private ArrayList<ImageItem> getData() {
        imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.blueprint_photo);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }

    public void imageClick(View v) {
        ImageView img = (ImageView) v;
        CommonUtils.transitToFragment(this, new BluePrintPhotoPreviewFragment(img.getId(), imageItems));
    }

    public void checkBoxClick(View v) {
        CheckBox cb = (CheckBox) v;
        int id = cb.getId();
        imageItems.get(id).setSelected(cb.isChecked());

        if(hasSelectedItem()) {
            this.menu.getItem(0).setVisible(true);
        } else {
            this.menu.getItem(0).setVisible(false);
        }
    }

    private boolean hasSelectedItem() {
        for(int i = 0; i < imageItems.size(); i++) {
            if(imageItems.get(i).isSelected()) {
                return true;
            }
        }

        return false;
    }
}
