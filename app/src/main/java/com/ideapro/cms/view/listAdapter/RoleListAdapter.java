package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.RoleEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class RoleListAdapter extends ArrayAdapter<RoleEntity> {

    private Activity activity;

    public RoleListAdapter(Context context, Activity activity, List<RoleEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RoleEntity entity = getItem(position);
        RoleListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_role_list_item, parent, false);

            viewHolder = new RoleListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RoleListViewHolder) convertView.getTag();
        }

        convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));

        viewHolder.tvwRole.setText(entity.name);
        viewHolder.tvwDescription.setText(entity.description);

        // Return the completed view to render on screen
        return convertView;
    }

    private class RoleListViewHolder {
        TextView tvwRole;
        TextView tvwDescription;

        public RoleListViewHolder(View view) {
            tvwRole = (TextView) view.findViewById(R.id.tvwRole);
            tvwDescription = (TextView) view.findViewById(R.id.tvwDescription);
        }
    }
}