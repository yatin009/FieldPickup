package io.webguru.fieldpickup.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.ApiHandler.ApiHandler;
import io.webguru.fieldpickup.ApiHandler.ApiRequestHandler;
import io.webguru.fieldpickup.ApiHandler.dto.LoginDataDTO;
import io.webguru.fieldpickup.POJO.User;
import io.webguru.fieldpickup.R;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    @Bind(R.id.parent_layout)
    LinearLayout parentLayout;
//    @Bind(R.id.signup_layout)
//    RelativeLayout signUpLayout;
    @Bind(R.id.editTextUsername)
    EditText username;

    @Bind(R.id.editTextPassword)
    EditText password;

    @Bind(R.id.check_remember)
    CheckBox mRememberCheckBox;

    Context context;


    private TextView userDisplayNameView;
    private TextView userDisplayEmailView;

    int REQUEST_PERMISSIONS;

    private String TAG = "SPLASHSCREEN";

    private boolean isSignIn = false, isRememberMe = false;
    String passwordValue;
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        context = this;
        Log.d(TAG,"checkLoginStatus() >>>> "+checkLoginStatus());
        if(checkLoginStatus()) {
            redirectToMainActivity();
            return;
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = parentLayout.getRootView().getHeight() - parentLayout.getHeight();
//                if (heightDiff > dpToPx(LoginActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
//                    // ... do something here
//                    signUpLayout.setVisibility(View.GONE);
//                } else {
//                    signUpLayout.setVisibility(View.VISIBLE);
//                }
            }
        });
    }

    private boolean checkLoginStatus(){
        SharedPreferences sharedPref = (LoginActivity.this).getSharedPreferences(getString(R.string.login_status),Context.MODE_PRIVATE);
        return sharedPref.getBoolean("isLogged", false);
    }

    public float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @OnClick(R.id.login_button)
    public void loginClick(){
        if(!isUsernameValid(username.getText().toString())){
            username.setError("Invalid Username");
            username.requestFocus();
            return;
        }
        if(!isPasswordValid(password.getText().toString())){
            password.setError("Invalid Password");
            password.requestFocus();
            return;
        }

        showProgressDialog();
        new UserLoginTask(username.getText().toString(), password.getText().toString()).execute();
    }

    private boolean isUsernameValid(String user) {
        //TODO: Replace this with your own logic
        return !user.equals("");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String mUsername, String password) {
            this.mUsername = mUsername;
            this.mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            boolean isLoginSuccessFull = false;

            Integer statusCode = authenticateUserOnServer(mUsername,mPassword,LoginActivity.this);
//            Integer statusCode = 200;

            if(statusCode.equals(200)) {
                SharedPreferences sharedPref = (LoginActivity.this).getSharedPreferences(getString(R.string.login_status), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("isLogged", true);
                editor.apply();
                isLoginSuccessFull = true;
                ApiHandler.getLoginData(LoginActivity.this);
            }
            // TODO: register the new account here.
            return isLoginSuccessFull;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            hideProgressDialog();
            if (success) {
                redirectToMainActivity();
            } else {
                Toast.makeText(context, "Invalid username or password", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            hideProgressDialog();
        }
    }

    private void redirectToMainActivity(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Authenticating…");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void checkPermissions(){
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0){
            for(int i=0; i<grantResults.length; i++){
                if(grantResults[i] != 0){
                    this.finishAffinity();
                }
            }
        }
    }

    public static int authenticateUserOnServer(String username, String password, Context context) {

        List<NameValuePair> formData = new ArrayList<NameValuePair>();
        formData.add(new BasicNameValuePair("j_username", username));
        formData.add(new BasicNameValuePair("j_password", password));
        formData.add(new BasicNameValuePair("remember-me", "false"));
        formData.add(new BasicNameValuePair("submit", "Login"));
        HttpResponse httpResponse = ApiRequestHandler.makeServiceCall("app/authentication", formData, null, context);
//        HttpResponse httpResponse = null;
        int StatusCode = 401;
        try {
            String responseMessage = null;
            if (httpResponse != null) {
                StatusCode = httpResponse.getStatusLine().getStatusCode();

                if(StatusCode == 200){
                    SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.login_status), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Header[] headers = httpResponse.getHeaders("Set-Cookie");

                    String value = null;

                    for (Header h : headers) {
                        value = h.getValue().toString();
                        String[] param = value.split(";");
                        String[] val = param[0].split("=");
                        if(val[0].contains("JSESSIONID")){
                            editor.putString(context.getString(R.string.JSESSION_ID), val[1]);
                        } else if(val[0].contains("hazelcast.sessionId")){
                            editor.putString(context.getString(R.string.HAZELCAST_SESSION_ID), val[1]);
                        } else if(val[0].contains("CSRF-TOKEN")){
                            editor.putString(context.getString(R.string.CSRF_TOKEN), val[1]);
                        }
                    }

                    editor.putString(context.getString(R.string.DISPLAY_USER_NAME), "Default User");
                    editor.putString(context.getString(R.string.DISPLAY_USER_EMAIL), "default@saptransport.net");
                    editor.commit();
                    HttpResponse httpResponse1 = ApiRequestHandler.makeServiceCall("app/account", null, null, context);
                    responseMessage = EntityUtils.toString(httpResponse1.getEntity(), "UTF-8");
                    Log.i("Response------ ",responseMessage);
                    ObjectMapper objectMapper = new ObjectMapper();
                    User user = objectMapper.readValue(responseMessage, User.class);
                    SharedPreferences sharedPreferences1 = context.getSharedPreferences(context.getString(R.string.login_status), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString(context.getString(R.string.DISPLAY_USER_NAME), user.getFirstName() + " " + user.getLastName());
                    editor1.putString(context.getString(R.string.DISPLAY_USER_EMAIL), user.getEmail());
                    editor1.commit();
//                    ApiHandler.getLoginData(context);

                    return StatusCode;
//                    responseMessage = EntityUtils.toString(httpResponse1.getEntity());

                } else if (StatusCode == 401) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseMessage);
                        if (jsonObject.length() != 0) {
                            if (jsonObject.has("message")) {
                                if (jsonObject.getString("message").equalsIgnoreCase("Wrong credentials! Try again."))
                                    StatusCode = 401;
                                else if (jsonObject.getString("message").equalsIgnoreCase("User already logged-in. Logout first and try again."))
                                    StatusCode = 1201;
                                else if (jsonObject.getString("message").equalsIgnoreCase("User locked! Try after 15 minutes."))
                                    StatusCode = 1203;
                                return StatusCode;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            StatusCode = 500;
            e.printStackTrace();
        }
        return StatusCode;
    }

}

