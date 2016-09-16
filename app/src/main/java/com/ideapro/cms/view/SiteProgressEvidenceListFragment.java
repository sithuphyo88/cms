package com.ideapro.cms.view;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.ideapro.cms.view.listAdapter.SiteProgressEvidenceListAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SiteProgressEvidenceListFragment extends Fragment {
    private static final String BARG_SITE_ID = "site_id";
    private static final String BARG_TASK_ID = "task_id";
    private static final String BARG_PROJECT_ID = "project_id";
    private static final String BARG_PROGRESS_ID = "progress_id";

    View view;
    ArrayList<ImageItem> imageItems;
    SiteProgressHistoryEntity entity;
    ImageButton imgRight;
    private GridView gridView;
    private SiteProgressEvidenceListAdapter adapter;
    Menu menu;

    public SiteProgressEvidenceListFragment() {
    }

    public SiteProgressEvidenceListFragment(SiteProgressHistoryEntity entity) {
        this.entity = entity;
    }

    public static SiteProgressEvidenceListFragment newInstance(String projectId, String siteId, String taskId, String progressId) {

        Bundle args = new Bundle();
        args.putString(BARG_PROJECT_ID, projectId);
        args.putString(BARG_SITE_ID, siteId);
        args.putString(BARG_TASK_ID, taskId);
        args.putString(BARG_PROGRESS_ID, progressId);

        SiteProgressEvidenceListFragment fragment = new SiteProgressEvidenceListFragment();
        fragment.setArguments(args);
        return fragment;
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
        inflater.inflate(R.menu.menu_share, menu);
        this.menu.getItem(0).setVisible(false);
        getActivity().setTitle(entity.siteName + " - " + entity.progress + "% - " + getString(R.string.label_evidences));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                ArrayList<Uri> imageUris = new ArrayList<>();
                for (int i = 0; i < imageItems.size(); i++) {
                    if (imageItems.get(i).isSelected()) {
                        imageUris.add(Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), imageItems.get(i).getImage(), "", null)));
                    }
                }

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "Share images to.."));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void bindData() {
        try {
            if (imageItems == null) {
                imageItems = getData();
            }

            gridView = (GridView) view.findViewById(R.id.gridView);
            adapter = new SiteProgressEvidenceListAdapter(this, view.getContext(), R.layout.fragment_site_progress_evidence_list_item, imageItems);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
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

    public void commentClick(View v) {
        ImageButton img = (ImageButton) v;
        CommonUtils.transitToFragment(this, new CommentListFragment());
    }

    public void checkBoxClick(View v) {
        CheckBox cb = (CheckBox) v;
        int id = cb.getId();
        imageItems.get(id).setSelected(cb.isChecked());

        if (hasSelectedItem()) {
            this.menu.getItem(0).setVisible(true);
        } else {
            this.menu.getItem(0).setVisible(false);
        }
    }

    private boolean hasSelectedItem() {
        for (int i = 0; i < imageItems.size(); i++) {
            if (imageItems.get(i).isSelected()) {
                return true;
            }
        }

        return false;
    }

    private String getProjectId() {
        Bundle bundle = getArguments();
        String projectId = "";
        if (bundle != null) {
            projectId = bundle.getString(BARG_PROJECT_ID);
        }
        return projectId;
    }

    private String getSiteId() {
        Bundle bundle = getArguments();
        String siteId = "";
        if (bundle != null) {
            siteId = bundle.getString(BARG_SITE_ID);
        }
        return siteId;
    }

    private String getTaskId() {
        Bundle bundle = getArguments();
        String taskId = "";
        if (bundle != null) {
            taskId = bundle.getString(BARG_TASK_ID);
        }
        return taskId;
    }

    private String getProgressId() {
        Bundle bundle = getArguments();
        String progressId = "";
        if (bundle != null) {
            progressId = bundle.getString(BARG_PROGRESS_ID);
        }
        return progressId;
    }
}
