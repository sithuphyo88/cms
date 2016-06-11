package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.MaterialEntity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by stp on 4/12/2015.
 */
public class MaterialListAdapter extends ArrayAdapter<MaterialEntity> {

    private Activity activity;

    public MaterialListAdapter(Context context, Activity activity, List<MaterialEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MaterialEntity entity = getItem(position);
        MaterialListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_material_list_item, parent, false);

            viewHolder = new MaterialListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MaterialListViewHolder) convertView.getTag();
        }

        convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));

        viewHolder.tvwMaterial.setText(entity.name);
        viewHolder.tvwDescription.setText(entity.description);

        // Return the completed view to render on screen
        return convertView;
    }

    private class MaterialListViewHolder {
        TextView tvwMaterial;
        TextView tvwDescription;
        CircleImageView material_image;

        public MaterialListViewHolder(View view) {
            tvwMaterial = (TextView) view.findViewById(R.id.tvwMaterial);
            tvwDescription = (TextView) view.findViewById(R.id.tvwDescription);
            material_image = (CircleImageView) view.findViewById(R.id.material_image);
        }
    }
}