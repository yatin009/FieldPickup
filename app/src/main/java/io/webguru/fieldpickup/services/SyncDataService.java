package io.webguru.fieldpickup.services;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;

import io.webguru.fieldpickup.Activities.MainActivity;
import io.webguru.fieldpickup.AndroidMultiPartEntity;
import io.webguru.fieldpickup.ApiHandler.ApiRequestHandler;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Fragments.BlankFragment;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.POJO.Tab;

/**
 * Created by panchanandmahto on 26/04/17.
 */
@SuppressWarnings("deprecation")
public class SyncDataService extends AsyncTask<String, Void, Boolean> {

    public static DefaultHttpClient httpClient;

    public static String DOMAIN = null;

    private int statusCode = 0;

    DocketDataSource docketDataSource;

    private ProgressDialog dialog = new ProgressDialog(GlobalFunction.context);

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Please wait... Data is syncing with server...");
        this.dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected Boolean doInBackground(String... params) {

        DOMAIN = GlobalFunction.DOMAIN;
        HttpResponse httpResponse = ApiRequestHandler.makeServiceCall("app/rest/device/sync", null, null, null);
        statusCode = httpResponse.getStatusLine().getStatusCode();
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        if (statusCode == 200) {
            GlobalFunction.openDocketDatabaseConnection(GlobalFunction.context);
            docketDataSource = GlobalFunction.docketDataSource;
            docketDataSource.open();
            docketDataSource.markDocketsAsSynced(MainActivity.docketIdsToSync);
            Tab tab = BlankFragment.getTab("Done");
            if (tab != null) {
                Fragment faFragment = tab.getFragment();
                faFragment.onStart();
            }
            Toast.makeText(GlobalFunction.context, "Synced Successfully", Toast.LENGTH_LONG).show();
        } else {
            if(statusCode == 401){
                Toast.makeText(GlobalFunction.context, "Try to Re-Login", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(GlobalFunction.context, "Failed to Sync", Toast.LENGTH_LONG).show();
            }
        }

    }

}
