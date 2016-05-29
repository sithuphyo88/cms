package com.ideapro.cms.view;


import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.BluePrintEntity;
import com.ideapro.cms.data.ImageItem;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listAdapter.BluePrintPhotoListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BluePrintPhotoListFragment extends Fragment {

    View view;
    List<BluePrintEntity> list;
    private GridView gridView;
    private BluePrintPhotoListAdapter adapter;
    ImageButton imgAdd;
    ArrayList<ImageItem> imageItems;

    public BluePrintPhotoListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blueprint_photo_list, container, false);
        bindData();
        setActionBar();
        initializeUI();

        return view;
    }

    private void setActionBar() {
        View customView = CommonUtils.setActionBarForFragment((ActionBarActivity)getActivity(),
                getString(R.string.label_manage_blue_print),
                R.mipmap.ic_search);

        ImageButton imgRight = (ImageButton) customView.findViewById(R.id.imgRight);
        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton imgButton = (ImageButton)v;
                int resId = (Integer)imgButton.getTag();

                if(resId == R.mipmap.ic_search) {

                } else if(resId == R.mipmap.ic_remove) {
                    if(hasSelectedItem()) {
                        CommonUtils.showConfirmDialogBox(getActivity(), getString(R.string.message_confirmation_delete),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i < imageItems.size(); i++) {
                                            if(imageItems.get(i).isSelected()) {
                                                imageItems.remove(i);
                                                i--;
                                            }
                                        }

                                        adapter.notifyDataSetChanged();
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });
                    }
                }
            }
        });
    }

    private void initializeUI() {
        imgAdd = (ImageButton) view.findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void bindData() {
        try {
            if(imageItems == null) {
                imageItems = new ArrayList<>();
            }

            gridView = (GridView) view.findViewById(R.id.gridView);

            adapter = new BluePrintPhotoListAdapter(this, view.getContext(), R.layout.fragment_blueprint_photo_list_item, imageItems);
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
            CommonUtils.setActionBarForFragment((ActionBarActivity)getActivity(),
                    getString(R.string.label_manage_blue_print),
                    R.mipmap.ic_remove);
        } else {
            CommonUtils.setActionBarForFragment((ActionBarActivity)getActivity(),
                    getString(R.string.label_manage_blue_print),
                    R.mipmap.ic_search);
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

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.label_take_blueprint)),1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            try {
                if (requestCode == 1) {
                    if(data.getData() != null) {
                        Uri uri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(),uri);
                        ImageItem item = new ImageItem(bitmap, "");
                        imageItems.add(item);
                    } else if(data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(),uri);
                            ImageItem mItem = new ImageItem(bitmap, "");
                            imageItems.add(mItem);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();
        }
    }
}
