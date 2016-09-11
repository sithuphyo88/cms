package com.ideapro.cms.view.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.ideapro.cms.R;
import com.ideapro.cms.cmsApp;

/**
 * Created by asus on 8/29/2016.
 */
public class BaseFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 101;
    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 102;

    private static final int REQUEST_IMAGE_CAPTURE = 1001;
    private static final int REQUEST_IMAGE_CAPTURE_FULL_RESOLUTION = 1002;
    private static final int REQUEST_SELECT_IMAGE_ABOVE_KITKAT = 1003;
    private static final int REQUEST_SELECT_IMAGE = 1004;

    private String numberToCall = null;
    private String mCurrentPhotoPath;

    private ProgressDialog mProgressDialog;

    protected void selectPicture() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            // Show only images, no videos or anything else
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            // Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), REQUEST_SELECT_IMAGE);
        } else {
            if (ActivityCompat.checkSelfPermission(cmsApp.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_READ_EXTERNAL_STORAGE);

                return;
            }

            Intent intent = new Intent();
            // Show only images, no videos or anything else
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            // Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), REQUEST_SELECT_IMAGE_ABOVE_KITKAT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap takenPicture = (Bitmap) extras.get("data");
                /*onPictureTaken(takenPicture);*/
            } else if (requestCode == REQUEST_IMAGE_CAPTURE_FULL_RESOLUTION) {
               /* onPictureTaken(mCurrentPhotoPath);*/
            } else if (requestCode == REQUEST_SELECT_IMAGE_ABOVE_KITKAT) {
                Uri originalUri = data.getData();
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // Check for the freshest data.
                this.getActivity().getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
                //onPictureSelect(originalUri);

                String id = originalUri.getLastPathSegment().split(":")[1];
                final String[] imageColumns = {MediaStore.Images.Media.DATA};
                final String imageOrderBy = null;

                Uri uri = getUri();

                String selectedImagePath = "path";

               /* Cursor imageCursor = managedQuery(uri, imageColumns,
                        MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);*/
               /* Cursor imageCursor = cmsApp.getContext().getContentResolver().query(uri, imageColumns, null, null, null);*/
                /* Cursor imageCursor = managedQuery(uri, imageColumns,
                        MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);*/

                String[] column = {MediaStore.Images.Media.DATA};

                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor imageCursor =getActivity(). getContentResolver().
                        query(uri,
                                column, sel, new String[]{id}, null);

                if (imageCursor.moveToFirst()) {
                    selectedImagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }

                onPictureTaken(selectedImagePath);
            } else if (requestCode == REQUEST_SELECT_IMAGE) {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = this.getActivity().getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String picturePath = cursor.getString(columnIndex); // returns null
                    cursor.close();

                    onPictureTaken(picturePath);
                }
            }
        }
    }

    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }


    public void onPictureTaken(Bitmap takenPicture) {

    }

    public void onPictureTaken(String localPath) {

    }
}
