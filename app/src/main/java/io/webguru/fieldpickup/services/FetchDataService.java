package io.webguru.fieldpickup.services;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.List;

import io.webguru.fieldpickup.Activities.MainActivity;
import io.webguru.fieldpickup.ApiHandler.ApiHandler;
import io.webguru.fieldpickup.ApiHandler.ApiRequestHandler;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Fragments.BlankFragment;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.Tab;

/**
 * Created by panchanandmahto on 29/04/17.
 */

public class FetchDataService extends AsyncTask<String, Void, Boolean> {

    public static String DOMAIN = null;
    private static DocketDataSource docketDataSource;

    public ProgressDialog mProgressDialog;
    int newDocketCount = 0;

    private ProgressDialog dialog = new ProgressDialog(GlobalFunction.context);

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Please wait... Fetching data from server... ");
        this.dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected Boolean doInBackground(String... params) {

        DOMAIN = GlobalFunction.DOMAIN;
        ApiHandler.getLoginData(GlobalFunction.context);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        Tab tab = BlankFragment.getTab("Pending");
        if (tab != null) {
            Fragment faFragment = tab.getFragment();
            faFragment.onStart();
        }
        if(newDocketCount > 0) {
            Toast.makeText(GlobalFunction.context, newDocketCount + " New Dockets Found", Toast.LENGTH_LONG).show();
        }
    }

}
