package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.PurchaseOrderEntity;
import com.ideapro.cms.data.SiteEntity;

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

        if(entity.receivedNumber.equals(entity.purchasedNumber)) {
            convertView.setBackgroundColor(Color.LTGRAY);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        viewHolder.tvwOrderNo.setText(entity.purchaseOrderNo);
        viewHolder.tvwPurchaseOrderDate.setText(entity.date);
        viewHolder.tvwReceivedNumber.setText(entity.receivedNumber);
        viewHolder.tvwOrderedNumber.setText(entity.purchasedNumber);

        // Return the completed view to render on screen
        return convertView;
    }

    private class PurchaseOrderListViewHolder {
        public PurchaseOrderListViewHolder(View view) {
            tvwOrderNo = (TextView) view.findViewById(R.id.tvwOrderNo);
            tvwPurchaseOrderDate = (TextView) view.findViewById(R.id.tvwPurchaseOrderDate);
            tvwReceivedNumber = (TextView) view.findViewById(R.id.tvwReceivedNumber);
            tvwOrderedNumber = (TextView) view.findViewById(R.id.tvwOrderedNumber);
        }

        TextView tvwOrderNo;
        TextView tvwPurchaseOrderDate;
        TextView tvwReceivedNumber;
        TextView tvwOrderedNumber;
    }
}