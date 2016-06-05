package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.ProjectEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class ProjectListAdapter extends ArrayAdapter<ProjectEntity> {

    private Activity activity;

    public ProjectListAdapter(Context context, Activity activity, List<ProjectEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ProjectEntity entity = getItem(position);
        SiteListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_project_list_item, parent, false);

            viewHolder = new SiteListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SiteListViewHolder)convertView.getTag();
        }

        if (position % 2 == 0) {
            convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item_even_shape));
        } else {
            convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item_odd_shape));
        }

        viewHolder.tvwProjectName.setText(entity.name);
        viewHolder.tvwStartDate.setText(entity.startDate);
        viewHolder.tvwEndDate.setText(entity.endDate);
        viewHolder.butProgress.setText(entity.progress + " %");
        viewHolder.proProgress.setProgress(Integer.parseInt(entity.progress));

        // Return the completed view to render on screen
        return convertView;
    }

    private class SiteListViewHolder {
        TextView tvwProjectName;
        TextView tvwStartDate;
        TextView tvwEndDate;
        Button butProgress;
        ProgressBar proProgress;
        public SiteListViewHolder(View view) {
            tvwProjectName = (TextView) view.findViewById(R.id.tvwProjectName);
            tvwStartDate = (TextView) view.findViewById(R.id.tvwStartDate);
            tvwEndDate = (TextView) view.findViewById(R.id.tvwEndDate);
            butProgress = (Button) view.findViewById(R.id.butProgress);
            proProgress = (ProgressBar) view.findViewById(R.id.proProgress);
        }
    }
}