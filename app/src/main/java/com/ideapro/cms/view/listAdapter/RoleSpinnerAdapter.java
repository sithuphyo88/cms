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
import com.ideapro.cms.view.UserAddFragment;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class RoleSpinnerAdapter extends ArrayAdapter<String> {

    private final List<Boolean> selectedList;
    private Activity activity;
    UserAddFragment fragment;

    public RoleSpinnerAdapter(UserAddFragment userAddFragment, Context context, Activity activity, String[] files, List<Boolean> selectedList) {
        super(context, 0, files);
        this.activity = activity;
        this.fragment = userAddFragment;
        this.selectedList = selectedList;
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getCustomView(position, cnvtView, prnt);
    }

    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        return getCustomView(pos, cnvtView, prnt);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String entity = getItem(position);
        RoleSpinnerViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_role, parent, false);

            viewHolder = new RoleSpinnerViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RoleSpinnerViewHolder) convertView.getTag();
        }

        viewHolder.tvwValue.setText(entity);

        viewHolder.chkChoose.setId(position);
        viewHolder.chkChoose.setChecked(selectedList.get(position));
        viewHolder.chkChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fragment.selectCheckBox(buttonView);
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }

    private class RoleSpinnerViewHolder {
        CheckBox chkChoose;
        TextView tvwValue;

        public RoleSpinnerViewHolder(View view) {
            chkChoose = (CheckBox) view.findViewById(R.id.chkChoose);
            tvwValue = (TextView) view.findViewById(R.id.tvwValue);
        }
    }
}