package com.ideapro.cms.view.listAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.data.CommentEntity;

import java.util.List;

/**
 * Created by stp on 4/12/2015.
 */
public class CommentListAdapter extends ArrayAdapter<CommentEntity> {

    private Activity activity;

    public CommentListAdapter(Context context, Activity activity, List<CommentEntity> files) {
        super(context, 0, files);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CommentEntity entity = getItem(position);
        CommentListViewHolder viewHolder = null;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_comment_list_item, parent, false);

            viewHolder = new CommentListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommentListViewHolder) convertView.getTag();
        }

        if (position % 2 == 0) {
            convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item));
        } else {
            convertView.setBackground(activity.getResources().getDrawable(R.drawable.list_item_odd));
        }

        viewHolder.tvwUserName.setText(entity.userName);
        viewHolder.tvwCommentDescription.setText(entity.commentDescription);
        viewHolder.tvwDateTime.setText(entity.dateTime);

        // Return the completed view to render on screen
        return convertView;
    }

    private class CommentListViewHolder {
        TextView tvwUserName;
        TextView tvwCommentDescription;
        TextView tvwDateTime;

        public CommentListViewHolder(View view) {
            tvwUserName = (TextView) view.findViewById(R.id.tvwUserName);
            tvwCommentDescription = (TextView) view.findViewById(R.id.tvwCommentDescription);
            tvwDateTime = (TextView) view.findViewById(R.id.tvwDateTime);
        }
    }
}