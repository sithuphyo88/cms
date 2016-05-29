package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.PurchaseOrderEntity;
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

        viewHolder.tvwSupplier.setText(entity.supplier);
        viewHolder.tvwMaterialCategory.setText(entity.materialCategory);
        viewHolder.tvwMaterialItem.setText(entity.materialItem);
        viewHolder.tvwUOM.setText(entity.uom);
        viewHolder.tvwQuantity.setText(entity.quantity);

        // Return the completed view to render on screen
        return convertView;
    }

    private class PurchaseOrderItemListViewHolder {
        public PurchaseOrderItemListViewHolder(View view) {
            tvwSupplier = (TextView) view.findViewById(R.id.tvwSupplier);
            tvwMaterialCategory = (TextView) view.findViewById(R.id.tvwMaterialCategory);
            tvwMaterialItem = (TextView) view.findViewById(R.id.tvwMaterialItem);
            tvwUOM = (TextView) view.findViewById(R.id.tvwUOM);
            tvwQuantity = (TextView) view.findViewById(R.id.tvwQuantity);
        }

        TextView tvwSupplier;
        TextView tvwMaterialCategory;
        TextView tvwMaterialItem;
        TextView tvwUOM;
        TextView tvwQuantity;
    }
}