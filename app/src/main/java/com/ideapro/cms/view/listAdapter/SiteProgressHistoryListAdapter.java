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
import com.ideapro.cms.data.SiteProgressHistoryEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class SiteProgressHistoryListAdapter extends ArrayAdapter<SiteProgressHistoryEntity> {

    private Activity activity;

    public SiteProgressHistoryListAdapter(Context context, Activity activity, List<SiteProgressHistoryEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SiteProgressHistoryEntity entity = getItem(position);
        SiteProgressHistoryListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_site_progress_list_item, parent, false);

            viewHolder = new SiteProgressHistoryListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SiteProgressHistoryListViewHolder)convertView.getTag();
        }

        convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));

        viewHolder.tvwDate.setText(entity.date);
        viewHolder.tvwEngineerName.setText(entity.engineerName);
        viewHolder.tvwDescription.setText(entity.description);
        viewHolder.butProgress.setText(entity.progress + " %");
        viewHolder.proProgress.setProgress(Integer.parseInt(entity.progress));

        // Return the completed view to render on screen
        return convertView;
    }

    private class SiteProgressHistoryListViewHolder {
        TextView tvwDate;
        TextView tvwEngineerName;
        TextView tvwDescription;
        Button butProgress;
        ProgressBar proProgress;
        public SiteProgressHistoryListViewHolder(View view) {
            tvwDate = (TextView) view.findViewById(R.id.tvwDate);
            tvwEngineerName = (TextView) view.findViewById(R.id.tvwEngineerName);
            tvwDescription = (TextView) view.findViewById(R.id.tvwDescription);
            butProgress = (Button) view.findViewById(R.id.butProgress);
            proProgress = (ProgressBar) view.findViewById(R.id.proProgress);
        }
    }
}