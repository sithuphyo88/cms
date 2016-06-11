package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.PurchaseOrderEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class PurchaseOrderListAdapter extends ArrayAdapter<PurchaseOrderEntity> {

    private Activity activity;

    public PurchaseOrderListAdapter(Context context, Activity activity, List<PurchaseOrderEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PurchaseOrderEntity entity = getItem(position);
        PurchaseOrderListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_purchase_order_list_item, parent, false);

            viewHolder = new PurchaseOrderListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PurchaseOrderListViewHolder)convertView.getTag();
        }

        if (entity.receivedNumber == entity.purchasedNumber) {
            convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item_odd));
        } else {
            convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));
        }

        viewHolder.tvwOrderNo.setText(entity.purchaseOrderNo);
        viewHolder.tvwPurchaseOrderDate.setText(entity.date);

        float value = Float.parseFloat(String.valueOf(entity.receivedNumber)) / Float.parseFloat(String.valueOf(entity.purchasedNumber));
        viewHolder.proProgress.setProgress((int) (value * 100));
        viewHolder.tvwProgressValue.setText((int) (value * 100) + " % (" + entity.receivedNumber + " / " + entity.purchasedNumber + ")");

        // Return the completed view to render on screen
        return convertView;
    }

    private class PurchaseOrderListViewHolder {
        TextView tvwOrderNo;
        TextView tvwPurchaseOrderDate;
        ProgressBar proProgress;
        TextView tvwProgressValue;
        public PurchaseOrderListViewHolder(View view) {
            tvwOrderNo = (TextView) view.findViewById(R.id.tvwOrderNo);
            tvwPurchaseOrderDate = (TextView) view.findViewById(R.id.tvwPurchaseOrderDate);
            proProgress = (ProgressBar) view.findViewById(R.id.proProgress);
            tvwProgressValue = (TextView) view.findViewById(R.id.tvwProgressValue);
        }
    }
}