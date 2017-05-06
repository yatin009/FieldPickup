package io.webguru.fieldpickup.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import io.webguru.fieldpickup.ApiHandler.ApiHandler;
import io.webguru.fieldpickup.ApiHandler.dto.DeviceDataDTO;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Fragments.BlankFragment;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.Product;
import io.webguru.fieldpickup.POJO.Tab;
import io.webguru.fieldpickup.R;
import io.webguru.fieldpickup.services.FetchDataService;
import io.webguru.fieldpickup.services.SyncDataService;

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
        GlobalFunction.context = context;
        GlobalFunction.intent = intent;
        if (!checkLoginStatus()) {
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
            syncUpdatesToServer(true);
        } else if (id == R.id.action_fetch_updates) {

            fetchUpdatesFromServer();
        }


        return super.onOptionsItemSelected(item);
    }

    private void syncUpdatesToServer(final boolean isToSyncToServer) {

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        docketDataSource = new DocketDataSource(context);
        docketDataSource.open();
        List<Docket> docketList = docketDataSource.getAllDockets();

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();

        if (docketList != null && !docketList.isEmpty()) {
            List<Long> docketIdsToSync = new ArrayList<Long>();
            List<Docket> docketsToBeSync = new ArrayList<Docket>();
            List<DeviceDataDTO> deviceDataDTOList = new ArrayList<DeviceDataDTO>();
            DeviceDataDTO deviceDataDTO = null;
            for (Docket docket : docketList) {
                if (docket.isPending() == 0 && docket.getIsSynced() == 0) {
                    docketIdsToSync.add(docket.getId());
                    docketsToBeSync.add(docket);
                    deviceDataDTO = new DeviceDataDTO(docket);
                    deviceDataDTO.setFeIMEICode(imei);
                    deviceDataDTO.setStatus(docket.getStatus());
                    deviceDataDTO.setStatusDescription(docket.getStatusDescription());
                    deviceDataDTO.setIsQualityCheckCleared(docket.getIsQcCheckCleared());
                    deviceDataDTOList.add(deviceDataDTO);
                }
            }

            String json = null;
            try {
                json = new ObjectMapper().writeValueAsString(deviceDataDTOList);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            checkForFileLocation();
            File file = new File(Environment.getExternalStorageDirectory() + "/Field Pickup/Backup/data.json");
            try {
                try (PrintStream out = new PrintStream(new FileOutputStream(file))) {
                    out.print(json);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> imageList = new ArrayList<String>();
            for (Docket docket : docketsToBeSync) {
                for (Product product : docket.getProducts()) {
                    String productDescription = product.getDescription();
                    productDescription = productDescription.replaceAll(" ", "_");
                    imageList.add(docket.getAwbNumber() + "_" + productDescription + "_1.jpeg");
                    imageList.add(docket.getAwbNumber() + "_" + productDescription + "_2.jpeg");
                    imageList.add(docket.getAwbNumber() + "_" + productDescription + "_3.jpeg");
                }
            }

            File sourceFile = null;
            String sourcePath = null;
            String destinationPath = null;
            for (String imageName : imageList) {
                sourcePath = Environment.getExternalStorageDirectory() + "/Field Pickup/Temp/" + imageName;
                destinationPath = Environment.getExternalStorageDirectory() + "/Field Pickup/Backup/" + imageName;
                sourceFile = new File(sourcePath);
                if (sourceFile.exists()) {
                    try {
                        InputStream in = new FileInputStream(sourcePath);
                        OutputStream out = new FileOutputStream(destinationPath);

                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.login_status), Context.MODE_PRIVATE);
            String USER_ID = sharedPreferences.getString(context.getString(R.string.CURRENT_USER_ID), null);
            String source = Environment.getExternalStorageDirectory() + "/Field Pickup/Backup";
            String destination = Environment.getExternalStorageDirectory() + "/Field Pickup/backup.zip";
            zipFolder(source, destination);

            if (isToSyncToServer) {
                try {
                    String JSESSION_ID = sharedPreferences.getString(context.getString(R.string.JSESSION_ID), null);
                    new SyncDataService().execute(destination, JSESSION_ID, USER_ID);

                    if (!docketIdsToSync.isEmpty()) {
                            docketDataSource.markDocketsAsSynced(docketIdsToSync);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        docketDataSource.close();
        Tab tab = BlankFragment.getTab("Done");
        if (tab != null) {
            Fragment faFragment = tab.getFragment();
            faFragment.onStart();
        }
        Toast.makeText(MainActivity.this, "Synced Successfully", Toast.LENGTH_LONG).show();
    }


    private void fetchUpdatesFromServer() {
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                if (mProgressDialog != null) {
//                    mProgressDialog.dismiss();
//                }
//
//                int newDockets = ApiHandler.getLoginData(GlobalFunction.context);
//
//                docketDataSource = new DocketDataSource(context);
//                docketDataSource.open();
//                List<Docket> docketList = docketDataSource.getAllDockets();
//                docketDataSource.close();
//                if(newDockets > 0) {
//                    GlobalFunction.showNotification(MainActivity.this, intent, newDockets + " New Dockets Found");
//                    Toast.makeText(MainActivity.this, "Fetched Successfully", Toast.LENGTH_LONG).show();
//                }
//            }


//        }, 2000);

        new FetchDataService().execute();

    }


    public static void zipFolder(String srcFolder, String destZipFile) {
        ZipOutputStream zip = null;
        FileOutputStream fileWriter = null;
        try {
            fileWriter = new FileOutputStream(destZipFile);
            zip = new ZipOutputStream(fileWriter);

            addFolderToZip("", srcFolder, zip);
            zip.flush();
            zip.close();
        } catch (Exception e) {

        }

    }

    static private void addFileToZip(String path, String srcFile, ZipOutputStream zip)
            throws Exception {

        File folder = new File(srcFile);
        if (folder.isDirectory()) {
            addFolderToZip(path, srcFile, zip);
        } else {
            byte[] buf = new byte[1024];
            int len;
            FileInputStream in = new FileInputStream(srcFile);
            zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
            while ((len = in.read(buf)) > 0) {
                zip.write(buf, 0, len);
            }
        }
    }

    static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip)
            throws Exception {
        File folder = new File(srcFolder);

        for (String fileName : folder.list()) {
            if (path.equals("")) {
                addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
            } else {
                addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
            }
        }
    }

    private void checkForFileLocation() {


        File direct = new File(Environment.getExternalStorageDirectory() + "/Field Pickup");
        if (!direct.exists()) {
            direct.mkdirs();
        }


        String path2 = "Backup";
        File backupFile = new File(direct, path2);
        if (!backupFile.exists()) {
            try {
                backupFile.mkdirs();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
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
//            showProgressDialog("Syncing Updates To Server…");
            syncUpdatesToServer(true);
        } else if (id == R.id.nav_fetch) {
//            showProgressDialog("Fetching Updates From Server…");
            fetchUpdatesFromServer();
        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedPref = (MainActivity.this).getSharedPreferences(getString(R.string.login_status), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("isLogged", false);
            editor.apply();
            syncUpdatesToServer(false);

            docketDataSource = new DocketDataSource(context);
            docketDataSource.open();
            docketDataSource.emptyTable();
            docketDataSource.close();

            File file = new File(Environment.getExternalStorageDirectory() + "/Field Pickup/Backup");
//            if(file.exists()){
//                String[]entries = file.list();
//                for(String s: entries){
//                    File currentFile = new File(file.getPath(),s);
//                    currentFile.delete();
//                }
////                file.delete();
//            }
//
//            file = new File(Environment.getExternalStorageDirectory() + "/Field Pickup/Temp");
//            if(file.exists()){
//                String[]entries = file.list();
//                for(String s: entries){
//                    File currentFile = new File(file.getPath(),s);
//                    currentFile.delete();
//                }
//            }

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

    private boolean checkLoginStatus() {
        SharedPreferences sharedPref = (MainActivity.this).getSharedPreferences(getString(R.string.login_status), Context.MODE_PRIVATE);
        return sharedPref.getBoolean("isLogged", false);
    }

    private void redirectToLoginActivity() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }


}
