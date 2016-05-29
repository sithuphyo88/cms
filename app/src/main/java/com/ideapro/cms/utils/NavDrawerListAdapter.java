package com.ideapro.cms.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ideapro.cms.R;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_main_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        // TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                navDrawerItems.get(position).getIcon());
        imgIcon.setImageBitmap(CommonUtils.getRoundedShape(icon, 100));

        // imgIcon.setImageResource(navDrawerItems.get(position).getIcon());

        String[] titleName = navDrawerItems.get(position).getTitle().split(",");
        txtTitle.setText(titleName[1]);

        if(titleName[0].equals("2")) {
            /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(30, 0, 0, 0);
            imgIcon.setLayoutParams(params);*/
            convertView.setPadding(50, 0, 0, 0);
            imgIcon.setPadding(5, 5, 5, 5);
            //txtTitle.setPadding(15, 0, 0, 0);
        }

        /*
        // displaying count
        // check whether it set visible or not
        if(navDrawerItems.get(position).getCounterVisibility()){
            txtCount.setText(navDrawerItems.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }*/

        return convertView;
    }

}
