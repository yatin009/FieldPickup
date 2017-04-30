package io.webguru.fieldpickup.services;

import android.os.AsyncTask;
import android.util.Log;

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

import io.webguru.fieldpickup.AndroidMultiPartEntity;
import io.webguru.fieldpickup.ApiHandler.ApiRequestHandler;
import io.webguru.fieldpickup.GlobalFunction;

/**
 * Created by panchanandmahto on 26/04/17.
 */
@SuppressWarnings("deprecation")
public class SyncDataService extends AsyncTask<String, Void, Boolean> {

    public static DefaultHttpClient httpClient;

    public static String DOMAIN = null;


    @Override
    protected Boolean doInBackground(String... params) {

        DOMAIN = GlobalFunction.DOMAIN;
        ApiRequestHandler.makeServiceCall("app/rest/device/sync", null,null,null);
//        doFileUpload(params[0]);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

    }

    public static String doFileUpload(String zipFile) {
        Log.i("SyncChanges", "Inside doFileUpload >>>>>");
        String response = "500,Failed";
        try {
            if (httpClient == null) {
                httpClient = new DefaultHttpClient();
                httpClient.setCookieStore(new BasicCookieStore());
            }
            httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
            HttpParams httpParameters = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 60 * 1000);
            HttpConnectionParams.setSoTimeout(httpParameters, 120 * 1000);

            HttpPost httpPost = new HttpPost(DOMAIN + "app/rest/device/sync");
            httpPost.setHeader("Accept-Encoding", "gzip");
            httpPost.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            File file = new File(zipFile);
            //MultipartEntity reqEntity = new MultipartEntity();
            AndroidMultiPartEntity reqEntity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {
                        @Override
                        public void transferred(long num) {
//                            sendBroadcastMessage("sending ... " + (int) ((num / (float) totalSize) * 100) + "%");
                        }
                    });
            FileBody bin1 = new FileBody(file);
            reqEntity.addPart("file", bin1);
            httpPost.setEntity(reqEntity);
            long totalSize = reqEntity.getContentLength();
            HttpResponse httpResponse = httpClient.execute(httpPost);
            String responseMessage = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            int status_code = httpResponse.getStatusLine().getStatusCode();
            Log.i("SyncChanges", "status_code >>>> " + status_code);
            if (status_code == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                response = status_code + "," + EntityUtils.toString(httpEntity);
            } if(status_code ==401){
                response = status_code + ", Failed to authenticate User";
            } else {
                response = status_code + ", Failed to sync update";
            }
        } catch (Exception e) {
            Log.i("SyncChanges", "Inside catch of doFileUpload >>>>>");
            e.printStackTrace();
        }
        return response;
    }

}
