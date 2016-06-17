package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.TaskEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class SiteTaskListAdapter extends ArrayAdapter<TaskEntity> {

    private Activity activity;

    public SiteTaskListAdapter(Context context, Activity activity, List<TaskEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TaskEntity entity = getItem(position);
        SiteTaskListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_site_task_list_item, parent, false);

            viewHolder = new SiteTaskListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SiteTaskListViewHolder) convertView.getTag();
        }

        convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));

        viewHolder.tvwTitle.setText(entity.title);
        viewHolder.tvwDescription.setText(entity.description);
        viewHolder.tvwDate.setText(entity.startDate + " ~ " + entity.endDate);
        viewHolder.tvwAssignee.setText(entity.assignee);

        // Return the completed view to render on screen
        return convertView;
    }

    private class SiteTaskListViewHolder {
        TextView tvwTitle;
        TextView tvwDescription;
        TextView tvwDate;
        TextView tvwAssignee;

        public SiteTaskListViewHolder(View view) {
            tvwTitle = (TextView) view.findViewById(R.id.tvwTitle);
            tvwDate = (TextView) view.findViewById(R.id.tvwDate);
            tvwAssignee = (TextView) view.findViewById(R.id.tvwAssignee);
            tvwDescription = (TextView) view.findViewById(R.id.tvwDescription);
        }
    }
}