package io.webguru.fieldpickup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.OnClick;

/**
 * Created by mahto on 25/1/17.
 */

public class LoginActivity extends AppCompatActivity {

    EditText userName;
    EditText password;
    public ProgressDialog mProgressDialog;
    public static String login = null;

    Button _loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(login != null){
            Intent i = new Intent(this, MainActivity.class);
            finish();
            startActivity(i);
        }
        _loginButton = (Button) findViewById(R.id.btn_login);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }


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
            Toast.makeText(this, "Login Successful...", Toast.LENGTH_LONG).show();
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
}
