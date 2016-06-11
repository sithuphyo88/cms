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
import com.ideapro.cms.data.UserEntity;
import com.ideapro.cms.view.EngineerListFragment;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class EngineerListAdapter extends ArrayAdapter<UserEntity> {

    EngineerListFragment fragment;
    private Activity activity;

    public EngineerListAdapter(EngineerListFragment fragment, Context context, Activity activity, List<UserEntity> files) {
        super(context, 0, files);
        this.fragment = fragment;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        UserEntity entity = getItem(position);
        UserListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_engineer_list_item, parent, false);

            viewHolder = new UserListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UserListViewHolder)convertView.getTag();
        }

        convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));

        viewHolder.chkEngineerName.setId(position);

        viewHolder.tvwEngineerName.setText(entity.name);
        viewHolder.chkEngineerName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fragment.selectCheckBox(buttonView);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    private class UserListViewHolder {
        TextView tvwEngineerName;
        CheckBox chkEngineerName;
        public UserListViewHolder(View view) {
            tvwEngineerName = (TextView) view.findViewById(R.id.tvwEngineerName);
            chkEngineerName = (CheckBox) view.findViewById(R.id.chkEngineerName);
        }
    }
}