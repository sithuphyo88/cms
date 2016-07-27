package com.ideapro.cms;
import android.app.Application;
import android.content.Context;

/**
 * Created by User on 7/27/2016.
 */
public class cmsApp extends Application {
    private static Context context;

    public static final String TAG = "cmsApp";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}


