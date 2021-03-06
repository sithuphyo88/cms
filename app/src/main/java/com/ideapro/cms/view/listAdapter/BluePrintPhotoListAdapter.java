package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ideapro.cms.R;
import com.ideapro.cms.data.ImageItem;
import com.ideapro.cms.view.BluePrintPhotoListFragment;

import java.util.ArrayList;

/**
 * Created by sithu on 5/21/2016.
 */
public class BluePrintPhotoListAdapter extends ArrayAdapter<ImageItem> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList<ImageItem>();
    private BluePrintPhotoListFragment fragment;

    public BluePrintPhotoListAdapter(BluePrintPhotoListFragment fragment, Context context, int layoutResourceId, ArrayList<ImageItem> data) {
        super(context, layoutResourceId, data);
        this.fragment = fragment;
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.chkImage = (CheckBox) row.findViewById(R.id.chkImage);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.imageTitle.setId(position);
        holder.image.setId(position);
        holder.chkImage.setId(position);

        holder.chkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.checkBoxClick(v);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.imageClick(v);
            }
        });

        ImageItem item = data.get(position);
        holder.imageTitle.setText(item.getTitle());
        /*holder.image.setImageBitmap(item.getImage());*/

        Glide.with(context)
                .load(item.getLocalPath())
                .asBitmap().centerCrop()
                .into(holder.image);
        holder.chkImage.setChecked(item.isSelected());
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
        CheckBox chkImage;
    }
}
