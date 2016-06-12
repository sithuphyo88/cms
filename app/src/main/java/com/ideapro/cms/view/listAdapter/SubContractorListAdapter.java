package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.SubContractorEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class SubContractorListAdapter extends ArrayAdapter<SubContractorEntity> {

    private Activity activity;

    public SubContractorListAdapter(Context context, Activity activity, List<SubContractorEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SubContractorEntity entity = getItem(position);
        SubContractorListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_sub_contractor_list_item, parent, false);

            viewHolder = new SubContractorListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SubContractorListViewHolder) convertView.getTag();
        }

        convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));

        viewHolder.tvwSubContractorName.setText(entity.name);
        viewHolder.tvwPhone.setText(entity.phone);
        viewHolder.tvwEmail.setText(entity.email);
        viewHolder.tvwAddress.setText(entity.address);

        // Return the completed view to render on screen
        return convertView;
    }

    private class SubContractorListViewHolder {
        TextView tvwSubContractorName;
        TextView tvwPhone;
        TextView tvwEmail;
        TextView tvwAddress;

        public SubContractorListViewHolder(View view) {
            tvwSubContractorName = (TextView) view.findViewById(R.id.tvwSubContractorName);
            tvwPhone = (TextView) view.findViewById(R.id.tvwPhone);
            tvwEmail = (TextView) view.findViewById(R.id.tvwEmail);
            tvwAddress = (TextView) view.findViewById(R.id.tvwAddress);
        }
    }
}