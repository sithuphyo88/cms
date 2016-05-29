package com.ideapro.cms.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by stp on 4/12/2015.
 */
public class RestfulClient extends AsyncTask<String, Void, Void> {

    private String serviceUrl;
    private String inputData;
    private String resultData;
    private RestfulMethod restfulMethod;

    // Current view
    Context context;

    // task completed event
    OnTaskCompleted taskCompleted;

    // task exception event
    OnTaskException taskException;

    boolean hasError;
    private ProgressDialog progressDialog;

    public RestfulClient(Context context, RestfulMethod method, String serviceUrl, String inputData,
                         OnTaskCompleted taskCompleted, OnTaskException taskException) {
        this.restfulMethod = method;
        this.serviceUrl = serviceUrl;
        this.inputData = inputData;
        this.taskCompleted = taskCompleted;
        this.taskException = taskException;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
           if(this.restfulMethod == RestfulMethod.GET) {
                this.resultData = CommonUtils.getHTPPRequest(this.serviceUrl, inputData);
            } else if(this.restfulMethod == RestfulMethod.POST){
               this.resultData = CommonUtils.postHTPPRequest(this.serviceUrl, inputData);
            } else if(this.restfulMethod == RestfulMethod.CheckURL) {
               this.resultData = String.valueOf(CommonUtils.checkUrl(this.serviceUrl));
           }
        } catch (Exception e) {
            hasError = true;
            dismissProgressDialog();
            if (this.taskException != null) {
                this.taskException.onTaskError(e);
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        this.dismissProgressDialog();
        if(this.taskCompleted != null && !hasError) {
            try {
                this.taskCompleted.onTaskCompleted(this.resultData);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}