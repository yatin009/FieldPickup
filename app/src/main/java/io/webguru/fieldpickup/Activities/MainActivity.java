package io.webguru.fieldpickup.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Fragments.BlankFragment;
import io.webguru.fieldpickup.Fragments.DoneDocketsFragments;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.Tab;
import io.webguru.fieldpickup.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ProgressDialog mProgressDialog;

    private TextView userDisplayNameView;
    private TextView userDisplayEmailView;

    private static DocketDataSource docketDataSource;

    Intent intent;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent = getIntent();
        context = this;
        if(!checkLoginStatus()) {
            redirectToLoginActivity();
            return;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = (MainActivity.this).getSharedPreferences(getString(R.string.login_status), Context.MODE_PRIVATE);
        String DISPLAY_USER_NAME = sharedPreferences.getString(this.getString(R.string.DISPLAY_USER_NAME), null);
        String DISPLAY_USER_EMAIL = sharedPreferences.getString(this.getString(R.string.DISPLAY_USER_EMAIL), null);

        View hView = navigationView.getHeaderView(0);
        userDisplayNameView = (TextView) hView.findViewById(R.id.user_display_name);
        userDisplayNameView.setText(DISPLAY_USER_NAME);
        userDisplayEmailView = (TextView) hView.findViewById(R.id.user_display_email);
        userDisplayEmailView.setText(DISPLAY_USER_EMAIL);

        BlankFragment fragment = new BlankFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fragment);
        fragmentTransaction.commit();
//        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync_updates) {
            showProgressDialog("Syncing Updates To Server…");
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                    docketDataSource = new DocketDataSource(context);
                    docketDataSource.open();
                    List<Docket> docketList = docketDataSource.getAllDockets();
                    if(docketList != null && !docketList.isEmpty()){
                        List<Long> docketIdsToSync = new ArrayList<Long>();
                        for(Docket docket : docketList){
                            if(docket.isPending() == 0 && docket.getIsSynced() == 0){
                                docketIdsToSync.add(docket.getId());
                            }
                        }
                        if(!docketIdsToSync.isEmpty()){
                            docketDataSource.markDocketsAsSynced(docketIdsToSync);
                        }
                    }
                    Tab tab = BlankFragment.getTab("Done");
                    if(tab != null){
                        Fragment faFragment = tab.getFragment();
                        faFragment.onStart();
                    }
                    Toast.makeText(MainActivity.this, "Synced Successfully", Toast.LENGTH_LONG).show();
                }
            }, 2000);
//            return true;
        } else if (id == R.id.action_fetch_updates) {
            showProgressDialog("Fetching Updates From Server…");
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                    GlobalFunction.showNotification(MainActivity.this, intent,"3 New Dockets Found");
                    Toast.makeText(MainActivity.this, "Fetched Successfully", Toast.LENGTH_LONG).show();
                }
            }, 2000);
//            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment;
        FragmentTransaction fragmentTransaction;
        int id = item.getItemId();
        if (id == R.id.nav_docket) {
            fragment = new BlankFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_sync) {
            showProgressDialog("Syncing Updates To Server…");
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                    docketDataSource = new DocketDataSource(context);
                    docketDataSource.open();
                    List<Docket> docketList = docketDataSource.getAllDockets();
                    if(docketList != null && !docketList.isEmpty()){
                        List<Long> docketIdsToSync = new ArrayList<Long>();
                        for(Docket docket : docketList){
                            if(docket.isPending() == 0 && docket.getIsSynced() == 0){
                                docketIdsToSync.add(docket.getId());
                            }
                        }
                        if(!docketIdsToSync.isEmpty()){
                            docketDataSource.markDocketsAsSynced(docketIdsToSync);
                        }
                    }
                    docketDataSource.open();
                    Tab tab = BlankFragment.getTab("Done");
                    if(tab != null){
                        Fragment faFragment = tab.getFragment();
                        faFragment.onStart();
                    }
                    Toast.makeText(MainActivity.this, "Synced Successfully", Toast.LENGTH_LONG).show();
                }
            }, 2000);
        } else if (id == R.id.nav_fetch) {
            showProgressDialog("Fetching Updates From Server…");
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                    GlobalFunction.showNotification(MainActivity.this, intent, "5 New Dockets Found");
                    Toast.makeText(MainActivity.this, "Fetched Successfully", Toast.LENGTH_LONG).show();
                }
            }, 2000);
        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedPref = (MainActivity.this).getSharedPreferences(getString(R.string.login_status), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("isLogged", false);
            editor.apply();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private boolean checkLoginStatus(){
        SharedPreferences sharedPref = (MainActivity.this).getSharedPreferences(getString(R.string.login_status),Context.MODE_PRIVATE);
        return sharedPref.getBoolean("isLogged", false);
    }

    private void redirectToLoginActivity(){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }


}
