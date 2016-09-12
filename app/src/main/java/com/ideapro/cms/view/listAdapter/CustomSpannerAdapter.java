package com.ideapro.cms.view.listAdapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Objects;


/**
 * Created by asus on 9/12/2016.
 */
public class CustomSpannerAdapter extends ArrayAdapter<String> {


    public CustomSpannerAdapter(Context context, int resource, List<String> strings) {
        super(context, resource, strings);
    }

    @Override
    public int getCount() {
        // don't display last item. It is used as hint.
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}
