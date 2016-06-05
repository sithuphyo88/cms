package com.ideapro.cms.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.ideapro.cms.R;
import com.ideapro.cms.data.ImageItem;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.listener.OnSwipeTouchListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BluePrintPhotoPreviewFragment extends Fragment {

    View view;
    List<ImageItem> items;
    int selectedPosition;

    public BluePrintPhotoPreviewFragment() {
    }

    public BluePrintPhotoPreviewFragment(int selectedPosition, List<ImageItem> items) {
        this.items = items;
        this.selectedPosition = selectedPosition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blueprint_photo_preview, container, false);

        if(items != null) {
            TextView titleTextView = (TextView) view.findViewById(R.id.title);
            titleTextView.setText(items.get(selectedPosition).getTitle());

            final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) view.findViewById(R.id.image);
            imageView.setZoomEnabled(true);
            imageView.setMaxScale(50);
            imageView.setDoubleTapZoomDpi(10);
            imageView.setImage(ImageSource.bitmap(items.get(selectedPosition).getImage()));

            imageView.setOnTouchListener(new OnSwipeTouchListener(view.getContext()){
                public void onSwipeTop() {

                }
                public void onSwipeRight() {
                    if(selectedPosition < items.size()) {
                        // imageView.setImage(ImageSource.bitmap(items.get(selectedPosition++).getImage()));
                    }
                }
                public void onSwipeLeft() {
                    if(selectedPosition > 0) {
                        // imageView.setImage(ImageSource.bitmap(items.get(selectedPosition--).getImage()));

                    }
                }
                public void onSwipeBottom() {

                }
            });

            ImageButton imgButComment = (ImageButton) view.findViewById(R.id.imgButComment);
            imgButComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.transitToFragment(CommonUtils.getVisibleFragment(getFragmentManager()), new CommentListFragment());
                }
            });
        }

        return view;
    }
}
