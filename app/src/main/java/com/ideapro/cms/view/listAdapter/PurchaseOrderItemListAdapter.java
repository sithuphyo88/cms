package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.PurchaseOrderItemEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class PurchaseOrderItemListAdapter extends ArrayAdapter<PurchaseOrderItemEntity> {

    private Activity activity;

    public PurchaseOrderItemListAdapter(Context context, Activity activity, List<PurchaseOrderItemEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PurchaseOrderItemEntity entity = getItem(position);
        PurchaseOrderItemListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_purchase_order_item_list_item, parent, false);

            viewHolder = new PurchaseOrderItemListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PurchaseOrderItemListViewHolder)convertView.getTag();
        }

        convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));

        viewHolder.tvwTargetDate.setText(entity.targetedDate);
        viewHolder.tvwMaterialCategory.setText(entity.materialCategory);
        viewHolder.tvwMaterialItem.setText(entity.materialItem);
        viewHolder.tvwUOM.setText(entity.uom);
        viewHolder.tvwOrderedQuantity.setText(String.valueOf(entity.orderedQuantity));
        viewHolder.tvwReceivedQuantity.setText(String.valueOf(entity.receivedQuantity));

        // Return the completed view to render on screen
        return convertView;
    }

    private class PurchaseOrderItemListViewHolder {
        TextView tvwTargetDate;
        TextView tvwMaterialCategory;
        TextView tvwMaterialItem;
        TextView tvwUOM;
        TextView tvwOrderedQuantity;
        TextView tvwReceivedQuantity;
        public PurchaseOrderItemListViewHolder(View view) {
            tvwTargetDate = (TextView) view.findViewById(R.id.tvwTargetDate);
            tvwMaterialCategory = (TextView) view.findViewById(R.id.tvwMaterialCategory);
            tvwMaterialItem = (TextView) view.findViewById(R.id.tvwMaterialItem);
            tvwUOM = (TextView) view.findViewById(R.id.tvwUOM);
            tvwOrderedQuantity = (TextView) view.findViewById(R.id.tvwOrderedQuantity);
            tvwReceivedQuantity = (TextView) view.findViewById(R.id.tvwReceivedQuantity);
        }
    }
}