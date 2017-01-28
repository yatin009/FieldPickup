package io.webguru.fieldpickup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



/**
 * Created by mahto on 25/1/17.
 */

public class LoginActivity extends AppCompatActivity {

    EditText userName;
    EditText password;
    public ProgressDialog mProgressDialog;
    public static String login = null;

    Button _loginButton;

    int REQUEST_PERMISSIONS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(login != null){
            Intent i = new Intent(this, MainActivity.class);
            finish();
            startActivity(i);
        }

        userName = (EditText) findViewById(R.id.input_username);
        password = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login(){
        userName = (EditText) findViewById(R.id.input_username);
        password = (EditText) findViewById(R.id.input_password);
        final String user = userName.getText().toString();
        final String psw = password.getText().toString();
        login = user;

        showProgressDialog();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        onLoginSuccess(user,psw);
                    }
                }, 2000);

    }
    public void onLoginSuccess(String user, String psw){
        hideProgressDialog();
//        if(user.equals("admin") && psw.equals("admin")){
            Intent i = new Intent(this, MainActivity.class);
            finish();
            startActivity(i);
            Toast.makeText(this, "Login Successful...", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Invalid Username or password!", Toast.LENGTH_LONG).show();
//        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Authenticating...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }


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
