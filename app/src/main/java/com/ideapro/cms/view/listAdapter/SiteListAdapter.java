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
import com.ideapro.cms.data.SiteEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class SiteListAdapter extends ArrayAdapter<SiteEntity> {

    private Activity activity;

    public SiteListAdapter(Context context, Activity activity, List<SiteEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SiteEntity entity = getItem(position);
        SiteListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_site_list_item, parent, false);

            viewHolder = new SiteListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SiteListViewHolder)convertView.getTag();
        }

        viewHolder.tvwDesignName.setText(entity.name);
        viewHolder.tvwStartDate.setText(entity.startDate);
        viewHolder.tvwEndDate.setText(entity.endDate);
        viewHolder.butProgress.setText(entity.progress + " %");
        viewHolder.proProgress.setProgress(Integer.parseInt(entity.progress));

        // Return the completed view to render on screen
        return convertView;
    }

    private class SiteListViewHolder {
        public SiteListViewHolder(View view) {
            tvwDesignName = (TextView) view.findViewById(R.id.tvwSiteName);
            tvwStartDate = (TextView) view.findViewById(R.id.tvwStartDate);
            tvwEndDate = (TextView) view.findViewById(R.id.tvwEndDate);
            butProgress = (Button) view.findViewById(R.id.butProgress);
            proProgress = (ProgressBar) view.findViewById(R.id.proProgress);
        }

        TextView tvwDesignName;
        TextView tvwStartDate;
        TextView tvwEndDate;
        Button butProgress;
        ProgressBar proProgress;
    }
}