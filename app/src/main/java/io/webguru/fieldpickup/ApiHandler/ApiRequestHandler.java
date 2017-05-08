package io.webguru.fieldpickup.ApiHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import io.webguru.fieldpickup.Activities.LoginActivity;
import io.webguru.fieldpickup.AndroidMultiPartEntity;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.R;

/**
 * Created by mahto on 9/2/17.
 */
@SuppressWarnings("deprecation")
public class ApiRequestHandler {

    public static DefaultHttpClient httpClient;
    private  static String DOMAIN = GlobalFunction.DOMAIN;

    public static HttpResponse makeServiceCall(String url, List<NameValuePair> formData, String requestBody, Context context) {
        HttpResponse httpResponse = null;
        try {
            url = DOMAIN + url;


            if (httpClient == null) {
                httpClient = new DefaultHttpClient();
                httpClient.setCookieStore(new BasicCookieStore());
            }
            ApiRequestHandler.httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
            HttpParams httpParameters = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 60 * 1000);
            HttpConnectionParams.setSoTimeout(httpParameters, 120 * 1000);

            if(url.contains("rest/device/sync")){
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Accept-Encoding", "gzip");
                httpPost.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
                File file = new File(Environment.getExternalStorageDirectory() + "/Field Pickup/backup.zip");
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
                httpResponse = httpClient.execute(httpPost);
            } else
            if (formData != null || requestBody != null) {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Accept-Encoding", "gzip");
                httpPost.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
                if (requestBody != null) {
                    httpPost.setEntity(new StringEntity(requestBody, HTTP.UTF_8));
                } else if (formData != null) {
                    httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    httpPost.setEntity(new UrlEncodedFormEntity(formData, HTTP.UTF_8));
                }
                httpResponse = httpClient.execute(httpPost);
            } else {
                String params;
                HttpGet httpGet;
                if (formData != null) {
                    params = URLEncodedUtils.format(formData, HTTP.UTF_8);
                    httpGet = new HttpGet(url + "?" + params);
                } else {
                    httpGet = new HttpGet(url);
                }
                httpGet.setHeader("Accept", "application/json");
                httpGet.setHeader("Accept-Encoding", "gzip");
                httpGet.setHeader("Accept-Language:","en-US,en;");
                httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
                httpResponse = httpClient.execute(httpGet);
            }
            HttpEntity entity = httpResponse.getEntity();
            if (entity.getContentEncoding() != null && (entity.getContentEncoding().toString().equalsIgnoreCase("gzip") || entity.getContentEncoding().toString().contains("gzip"))) {
                httpResponse.setEntity(new GzipDecompressingEntity(httpResponse.getEntity()));
            }

            if(httpResponse.getStatusLine().getStatusCode() == 401){
                int statusCode = LoginActivity.reLogin();
                if(statusCode == 200){
                    httpResponse = makeServiceCall(url, formData, requestBody, context);
                } else if( statusCode == 0){
                    return null;
                }
            }
            return (httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage() != null && e.getMessage().contains("timed out")){

                HttpResponseFactory factory = new DefaultHttpResponseFactory();
                httpResponse = factory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
                return httpResponse;
            }
        }
        return null;
    }



}
