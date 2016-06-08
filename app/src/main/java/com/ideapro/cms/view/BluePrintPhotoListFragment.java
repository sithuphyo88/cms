package com.ideapro.cms.view;


import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
    Menu menu;

    public BluePrintPhotoListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blueprint_photo_list, container, false);
        setHasOptionsMenu(true);
        bindData();
        initializeUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_search, menu);
        getActivity().setTitle(getString(R.string.label_manage_blue_print));
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                return  true;

            case R.id.action_remove:
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
                return  true;

            default :
                return super.onOptionsItemSelected(item);
        }
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

    public void imageClick(View v) {
        ImageView img = (ImageView) v;
        CommonUtils.transitToFragment(this, new BluePrintPhotoPreviewFragment(img.getId(), imageItems));
    }

    public void checkBoxClick(View v) {
        CheckBox cb = (CheckBox) v;
        int id = cb.getId();
        imageItems.get(id).setSelected(cb.isChecked());

        if(hasSelectedItem()) {
            getActivity().getMenuInflater().inflate(R.menu.menu_remove, this.menu);
        } else {
            getActivity().getMenuInflater().inflate(R.menu.menu_search, this.menu);
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
