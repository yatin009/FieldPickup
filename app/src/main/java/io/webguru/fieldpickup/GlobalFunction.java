package io.webguru.fieldpickup;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContentResolverCompat;

import com.google.gson.Gson;
import com.google.gson.internal.Streams;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import io.webguru.fieldpickup.Activities.MainActivity;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;
import io.webguru.fieldpickup.POJO.Product;
import io.webguru.fieldpickup.POJO.Question;

/**
 * Created by yatin on 21/01/17.
 */

public class GlobalFunction {

    public static DocketDataSource docketDataSource;
    private static FieldDataDataSource fieldDataDataSource;

    public static String DOMAIN = "http://192.168.0.2:8081/";
//    public static String DOMAIN = "http://staging.saplogistics.in/";
//    public static String DOMAIN = "http://saplogistics.in/";
    public static Context context;
    public static Intent intent;
    public static ProgressDialog mProgressDialog;


    public static void openDocketDatabaseConnection(Context context) {
        if (docketDataSource == null) {
            docketDataSource = new DocketDataSource(context);
            docketDataSource.open();
        }
    }

    private static void openFieldDataDatabaseConnection(Context context) {
        if (fieldDataDataSource == null) {
            fieldDataDataSource = new FieldDataDataSource(context);
            fieldDataDataSource.open();
        }
    }

    public static ArrayList<Docket> getDocketList(Integer isPending, Context context) {
        openDocketDatabaseConnection(context);
        ArrayList<Docket> docketList = docketDataSource.getAllDockets();
        return docketList;
    }

    private static Docket insertDocket(Context context, String docketNumber, String customerName, String contactNumber, String address,
                                       String product, String pincode, String orderNumber) {

        openDocketDatabaseConnection(context);
        return docketDataSource.createDocket(docketNumber, customerName, contactNumber, address, product, 1, pincode, orderNumber,"Not Updated Yet", "Not Updated Yet","no");
    }

    public static ArrayList<Docket> createDocketIntoDB(ArrayList<Docket> dockets, Context context) {
        ArrayList<Docket> docketArrayList = new ArrayList<>();
        if (dockets != null) {
            openDocketDatabaseConnection(context);
            ArrayList<Docket> docketList = docketDataSource.getAllDockets();
            Map<String, Docket> docketMap = new HashMap<>();
            Docket docket1 = null;
            for (Docket docket : docketList) {
                docketMap.put(docket.getAwbNumber(), docket);
            }
            for (Docket docket : dockets) {
                if (!docketMap.containsKey(docket.getAwbNumber())) {
                    docket1 = insertDocket(context, docket.getAwbNumber(), docket.getCustomerName(), docket.getCustomerContact(), docket.getCustomerAddress(),
                            new Gson().toJson(docket.getProducts()), docket.getPincode(), docket.getOrderNumber());
                    docketArrayList.add(docket1);
                }
            }
        }
        return docketArrayList;
    }


    public static ArrayList<Docket> getDocketList(boolean isPending, Context context) {
        ArrayList<Docket> dockets = new ArrayList<>();
        openDocketDatabaseConnection(context);
        List<Docket> docketList = docketDataSource.getAllDockets();
        if (docketList != null && !docketList.isEmpty()) {
            for (Docket docket : docketList) {
                if (isPending) {
                    if (docket.isPending() == 1) {
                        dockets.add(docket);
                    }
                } else {
                    if (docket.isPending() == 0) {
                        dockets.add(docket);
                    }
                }
            }
        }
        return dockets;
    }

    public static FieldData getFieldData(Long docketId, Context context) throws IOException {
        openFieldDataDatabaseConnection(context);
        FieldData fieldData = fieldDataDataSource.getFieldData(docketId);
        return fieldData;
    }


    public static void showNotification(Intent intent, String message) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_shopping_cart)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.sap_logo_round))
                .setContentTitle("SAP Pickup")
                .setContentText(message)
                .setSound(uri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }



    public static Map<String, String> getReasonCodeMap(){
        Map<String, String> reasonCodeMap = new HashMap<>();
        reasonCodeMap.put("101", "Wrong Price");
        reasonCodeMap.put("102","Product is not in working condition");
        reasonCodeMap.put("103","Defective or not working product");
        reasonCodeMap.put("104", "Delivered completely different product than the ordered one");
        reasonCodeMap.put("105", "The product is not in warranty period");
        reasonCodeMap.put("106", "Package and product both are damaged");
        reasonCodeMap.put("107", "Only product is damaged");
        reasonCodeMap.put("108", "Wrong product is ordered by mistake");
        reasonCodeMap.put("109", "The quantity delivered is less than the ordered quantity.");
        reasonCodeMap.put("110", "The product is not of good quality");
        reasonCodeMap.put("111", "Color of the product is different from the ordered one.");
        reasonCodeMap.put("112", "Size of the product is different from the ordered one.");
        reasonCodeMap.put("113", "Style of the product is different from the ordered one.");
        reasonCodeMap.put("114", "The product is not authentic.");
        reasonCodeMap.put("115", "Some parts of the product are missing.");
        reasonCodeMap.put("116", "Used/old products");
        reasonCodeMap.put("117", "Others");

        return reasonCodeMap;
    }

    public static String checkForImageLocation() {

        String path = "Field Pickup";
        File dir = new File(path);
        if (dir.exists()) {
//            dir.delete();
        }
        try {
            File dir1 = new File(path);
            dir1.mkdirs();
        } catch (SecurityException e) {
            e.printStackTrace();
        }


        String path2 = "Temp";
        File dir2 = new File(dir, path2);
        if (!dir2.exists()) {
            try {
                dir2.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        String path3 = "Picked";
        File dir3 = new File(dir, path3);
        if (!dir3.exists()) {
            try {
                dir3.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }


        return path2;
    }

}
