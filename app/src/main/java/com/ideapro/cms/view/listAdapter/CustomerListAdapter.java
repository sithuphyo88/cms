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
import com.ideapro.cms.data.CustomerEntity;
import com.ideapro.cms.data.ProjectEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class CustomerListAdapter extends ArrayAdapter<CustomerEntity> {

    private Activity activity;

    public CustomerListAdapter(Context context, Activity activity, List<CustomerEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CustomerEntity entity = getItem(position);
        CustomerListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_customer_list_item, parent, false);

            viewHolder = new CustomerListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomerListViewHolder)convertView.getTag();
        }

        convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));

        viewHolder.tvwCustomerName.setText(entity.name);
        viewHolder.tvwPhone.setText(entity.phone);
        viewHolder.tvwEmail.setText(entity.email);
        viewHolder.tvwAddress.setText(entity.address);

        // Return the completed view to render on screen
        return convertView;
    }

    private class CustomerListViewHolder {
        TextView tvwCustomerName;
        TextView tvwPhone;
        TextView tvwEmail;
        TextView tvwAddress;
        public CustomerListViewHolder(View view) {
            tvwCustomerName = (TextView) view.findViewById(R.id.tvwCustomerName);
            tvwPhone = (TextView) view.findViewById(R.id.tvwPhone);
            tvwEmail = (TextView) view.findViewById(R.id.tvwEmail);
            tvwAddress = (TextView) view.findViewById(R.id.tvwAddress);
        }
    }
}