package io.webguru.fieldpickup.services;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import java.util.List;

import io.webguru.fieldpickup.Activities.MainActivity;
import io.webguru.fieldpickup.ApiHandler.ApiHandler;
import io.webguru.fieldpickup.ApiHandler.ApiRequestHandler;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.POJO.Docket;

/**
 * Created by panchanandmahto on 29/04/17.
 */

public class FetchDataService extends AsyncTask<String, Void, Boolean> {

    public static String DOMAIN = null;
    private static DocketDataSource docketDataSource;

    public ProgressDialog mProgressDialog;

    @Override
    protected Boolean doInBackground(String... params) {

        DOMAIN = GlobalFunction.DOMAIN;
//        showProgressDialog("Fetching Updates From Server…");
        int newDocketCount = ApiHandler.getLoginData(GlobalFunction.context);
        if(newDocketCount > 0){
//            GlobalFunction.showNotification(MainActivity.this, intent, newDockets + " New Dockets Found");
//            Toast.makeText(GlobalFunction.context, newDocketCount + " new dockets fetched", Toast.LENGTH_LONG).show();
        }
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//        }
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

    }

    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(GlobalFunction.context);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }


}
