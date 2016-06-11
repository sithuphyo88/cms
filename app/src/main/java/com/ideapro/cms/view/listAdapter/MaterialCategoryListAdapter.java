package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.MaterialCategoryEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class MaterialCategoryListAdapter extends ArrayAdapter<MaterialCategoryEntity> {

    private Activity activity;

    public MaterialCategoryListAdapter(Context context, Activity activity, List<MaterialCategoryEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MaterialCategoryEntity entity = getItem(position);
        MaterialCategoryListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_material_category_list_item, parent, false);

            viewHolder = new MaterialCategoryListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MaterialCategoryListViewHolder) convertView.getTag();
        }

        convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));

        viewHolder.tvwMaterialCategory.setText(entity.name);
        viewHolder.tvwDescription.setText(entity.description);

        // Return the completed view to render on screen
        return convertView;
    }

    private class MaterialCategoryListViewHolder {
        TextView tvwMaterialCategory;
        TextView tvwDescription;

        public MaterialCategoryListViewHolder(View view) {
            tvwMaterialCategory = (TextView) view.findViewById(R.id.tvwMaterialCategory);
            tvwDescription = (TextView) view.findViewById(R.id.tvwDescription);
        }
    }
}