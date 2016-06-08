package com.ideapro.cms.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ideapro.cms.R;
import com.ideapro.cms.data.UserEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CommonUtils {

    public static String TICKER_CHECKING_RESTFUL_SERVICE_URL = "%s/datasynservice/rest/ticket/checkTicket?code=%s&terminalId=%s&gateId=%s";
    public static String GET_TERMINAL_RESTFUL_SERVICE_URL = "%s/datasynservice/rest/ticket/getTerminal?terminalId=%s";
    public static UserEntity CurrentUser = new UserEntity();
    private static int timeoutConnection = 2 * 60 * 1000;

    public static String getWifiIpAddress(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return android.text.format.Formatter.formatIpAddress(ip);
    }

    public static String getMacAddress(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

    public static boolean hasWifiConnection(Context context) {
        // Check wifi connection
        String ipAddress = CommonUtils.getWifiIpAddress(context);
        return !ipAddress.equals("0.0.0.0");
    }

    /**
     * This method is used to send POST requests to the server.
     *
     * @param URL
     * @param parameter
     * @return result of server response
     */
    static public String postHTPPRequest(String URL, String parameter) {

        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        // The default value is zero, that means the timeout is not used.
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.

        HttpConnectionParams.setSoTimeout(httpParameters, timeoutConnection);
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpPost httppost = new HttpPost(URL);
        httppost.setHeader("Content-Type", "application/json");
        try {
            if (parameter != null) {
                StringEntity tmp = null;
                tmp = new StringEntity(parameter, "UTF-8");
                httppost.setEntity(tmp);
            }
            HttpResponse httpResponse = null;

            httpResponse = httpclient.execute(httppost);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {

                BufferedReader in =
                        new BufferedReader(new InputStreamReader(entity.getContent()));
                String line="";
                StringBuffer returnFromServer = new StringBuffer();

                while ((line=in.readLine())!=null)
                {
                    returnFromServer.append(line);
                }

                return returnFromServer.toString();
            }
        }
        catch (Exception e) {
            throw new Error(e);
        }
        return null;
    }

    /**
     * This method is used to send GET requests to the server.
     *
     * @param URL
     * @param parameter
     * @return result of server response
     */
    static public String getHTPPRequest(String URL, String parameter) {

        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        // The default value is zero, that means the timeout is not used.
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.

        HttpConnectionParams.setSoTimeout(httpParameters, timeoutConnection);
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpGet httpGet = new HttpGet(URL);
        httpGet.setHeader("Content-Type", "application/json");
        try {
            HttpResponse httpResponse = null;

            httpResponse = httpclient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {

                BufferedReader in =
                        new BufferedReader(new InputStreamReader(entity.getContent()));
                String line="";
                StringBuffer returnFromServer = new StringBuffer();

                while ((line=in.readLine())!=null)
                {
                    returnFromServer.append(line);
                }

                return returnFromServer.toString();
            }
        }
        catch (Exception e) {
            throw new Error(e);
        }
        return null;
    }

    public static boolean checkUrl(String url) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con =  (HttpURLConnection)(new URL(url).openConnection());
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void showAlertDialogBox(final Activity activity, final String alertMessage) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setTitle(activity.getString(R.string.message_title));

                alertDialogBuilder.setMessage(alertMessage);
                alertDialogBuilder.setPositiveButton(activity.getString(R.string.message_alert_box_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    public static void showConfirmDialogBox(final Activity activity, final String confirmMessage,
                                            final DialogInterface.OnClickListener okClickListener,
                                            final DialogInterface.OnClickListener cancelClickListener) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setTitle(activity.getString(R.string.message_title));
                alertDialogBuilder.setMessage(confirmMessage);
                alertDialogBuilder.setPositiveButton(activity.getString(R.string.label_confirm_box_ok), okClickListener);
                alertDialogBuilder.setNegativeButton(activity.getString(R.string.label_confirm_box_cancel), cancelClickListener);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    public static void showMessage(Context context, String message, int duration) {
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
        View view = toast.getView();
    }

    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage, int radius) {
        int targetWidth = radius;
        int targetHeight = radius;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    public static void transitToActivity(Context packageContext, Class destinationClass) {
        Intent intent = new Intent(packageContext, destinationClass);
        packageContext.startActivity(intent);
    }

    public static void transitToFragment(Fragment sourceFragment, Fragment destFragment){
        android.support.v4.app.FragmentTransaction ftr = sourceFragment.getFragmentManager().beginTransaction();
        ftr.replace(sourceFragment.getId(), destFragment);
        ftr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ftr.addToBackStack(null);
        ftr.commit();
    }

    public static Fragment getVisibleFragment(FragmentManager fragmentManager){
        List<Fragment> fragments = fragmentManager.getFragments();

        if(fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }
}
