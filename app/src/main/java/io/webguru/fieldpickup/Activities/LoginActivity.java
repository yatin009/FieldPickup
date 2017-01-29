package io.webguru.fieldpickup.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    int REQUEST_PERMISSIONS;

    private String TAG = "SPLASHSCREEN";

    private boolean isSignIn = false, isRememberMe = false;
    String passwordValue;
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
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

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return true;
            }

            SharedPreferences sharedPref = (LoginActivity.this).getSharedPreferences(getString(R.string.login_status),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("isLogged", true);
            editor.apply();
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            hideProgressDialog();
            if (success) {
                redirectToMainActivity();
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
            mProgressDialog.setMessage("Loading…");
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
}

