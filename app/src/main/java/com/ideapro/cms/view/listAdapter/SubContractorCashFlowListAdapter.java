package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.SubContractorCashFlowEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class SubContractorCashFlowListAdapter extends ArrayAdapter<SubContractorCashFlowEntity> {

    private Activity activity;

    public SubContractorCashFlowListAdapter(Context context, Activity activity, List<SubContractorCashFlowEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SubContractorCashFlowEntity entity = getItem(position);
        SubContractorCashFlowListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_sub_contractor_cash_flow_list_item, parent, false);

            viewHolder = new SubContractorCashFlowListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SubContractorCashFlowListViewHolder) convertView.getTag();
        }

        convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));

        viewHolder.tvwDate.setText(entity.date);
        viewHolder.tvwDescription.setText(entity.description);
        viewHolder.tvwCreditAmount.setText(String.valueOf(entity.creditAmount));
        viewHolder.tvwPaidAmount.setText(String.valueOf(entity.paidAmount));

        // Return the completed view to render on screen
        return convertView;
    }

    private class SubContractorCashFlowListViewHolder {
        TextView tvwDate;
        TextView tvwDescription;
        TextView tvwCreditAmount;
        TextView tvwPaidAmount;

        public SubContractorCashFlowListViewHolder(View view) {
            tvwDate = (TextView) view.findViewById(R.id.tvwDate);
            tvwDescription = (TextView) view.findViewById(R.id.tvwDescription);
            tvwCreditAmount = (TextView) view.findViewById(R.id.tvwCreditAmount);
            tvwPaidAmount = (TextView) view.findViewById(R.id.tvwPaidAmount);
        }
    }
}