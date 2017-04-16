package io.webguru.fieldpickup.ApiHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import io.webguru.fieldpickup.R;

/**
 * Created by mahto on 9/2/17.
 */

public class ApiRequestHandler {

    public static DefaultHttpClient httpClient;
    public static String DOMAIN = "http://192.168.0.5:8081/";
//    public static String DOMAIN = "http://staging.saplogistics.in";
//    public static String DOMAIN = "http://saplogistics.in/";

    public static HttpResponse makeServiceCall(String url, List<NameValuePair> formData, String requestBody, Context context) {
        try {
            url = DOMAIN + url;
            HttpResponse httpResponse = null;

            if (httpClient == null) {
                httpClient = new DefaultHttpClient();
                httpClient.setCookieStore(new BasicCookieStore());
            }
            ApiRequestHandler.httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
            HttpParams httpParameters = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 60 * 1000);
            HttpConnectionParams.setSoTimeout(httpParameters, 120 * 1000);

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
            return (httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
