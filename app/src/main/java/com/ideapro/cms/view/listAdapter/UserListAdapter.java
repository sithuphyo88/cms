package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.UserEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class UserListAdapter extends ArrayAdapter<UserEntity> {

    private Activity activity;

    public UserListAdapter(Context context, Activity activity, List<UserEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        UserEntity entity = getItem(position);
        UserListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_user_list_item, parent, false);

            viewHolder = new UserListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UserListViewHolder) convertView.getTag();
        }

        convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));

        viewHolder.tvwUserName.setText(entity.name);
        viewHolder.tvwPhone.setText(entity.phone);
        viewHolder.tvwEmail.setText(entity.email);
        viewHolder.tvwRole.setText(entity.role);

        // Return the completed view to render on screen
        return convertView;
    }

    private class UserListViewHolder {
        TextView tvwUserName;
        TextView tvwPhone;
        TextView tvwEmail;
        TextView tvwRole;

        public UserListViewHolder(View view) {
            tvwUserName = (TextView) view.findViewById(R.id.tvwUserName);
            tvwPhone = (TextView) view.findViewById(R.id.tvwPhone);
            tvwEmail = (TextView) view.findViewById(R.id.tvwEmail);
            tvwRole = (TextView) view.findViewById(R.id.tvwRole);
        }
    }
}