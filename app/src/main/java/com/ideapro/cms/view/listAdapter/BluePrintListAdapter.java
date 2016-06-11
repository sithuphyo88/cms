package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.BluePrintEntity;
import com.ideapro.cms.view.BluePrintListFragment;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class BluePrintListAdapter extends ArrayAdapter<BluePrintEntity> {

    BluePrintListFragment fragment;
    private Activity activity;

    public BluePrintListAdapter(BluePrintListFragment fragment, Context context, Activity activity, List<BluePrintEntity> files) {
        super(context, 0, files);
        this.fragment = fragment;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BluePrintEntity entity = getItem(position);
        BluePrintListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_blueprint_list_item, parent, false);

            viewHolder = new BluePrintListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BluePrintListViewHolder)convertView.getTag();
        }

        convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));

        viewHolder.chkDate.setId(position);
        viewHolder.chkDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fragment.checkBoxClick(buttonView);
            }
        });

        if(entity != null){
            viewHolder.tvwDate.setText(entity.name);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    private class BluePrintListViewHolder {
        TextView tvwDate;
        CheckBox chkDate;
        public BluePrintListViewHolder(View view) {
            tvwDate = (TextView) view.findViewById(R.id.tvwDate);
            chkDate = (CheckBox) view.findViewById(R.id.chkDate);
        }
    }
}