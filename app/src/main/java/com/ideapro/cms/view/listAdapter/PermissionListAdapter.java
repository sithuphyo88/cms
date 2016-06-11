package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.PermissionEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class PermissionListAdapter extends ArrayAdapter<PermissionEntity> {

    private Activity activity;

    public PermissionListAdapter(Context context, Activity activity, List<PermissionEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PermissionEntity entity = getItem(position);
        PermissionListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_permission_list_item, parent, false);

            viewHolder = new PermissionListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PermissionListViewHolder) convertView.getTag();
        }

        convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));

        viewHolder.chkPermission.setId(position);

        viewHolder.tvwPermission.setText(entity.title);
        viewHolder.tvwDescription.setText(entity.description);

        // Return the completed view to render on screen
        return convertView;
    }

    private class PermissionListViewHolder {
        TextView tvwPermission;
        TextView tvwDescription;
        CheckBox chkPermission;

        public PermissionListViewHolder(View view) {
            tvwDescription = (TextView) view.findViewById(R.id.tvwDescription);
            tvwPermission = (TextView) view.findViewById(R.id.tvwPermission);
            chkPermission = (CheckBox) view.findViewById(R.id.chkPermission);
        }
    }
}