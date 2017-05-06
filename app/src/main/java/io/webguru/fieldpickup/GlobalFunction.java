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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    private static DocketDataSource docketDataSource;
    private static FieldDataDataSource fieldDataDataSource;

    public static String DOMAIN = "http://192.168.0.2:8081/";
//    public static String DOMAIN = "http://staging.saplogistics.in/";
//    public static String DOMAIN = "http://saplogistics.in/";
    public static Context context;
    public static Intent intent;
    public static ProgressDialog mProgressDialog;


    private static void openDocketDatabaseConnection(Context context) {
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
                                       String product, String pincode) {

        openDocketDatabaseConnection(context);
        return docketDataSource.createDocket(docketNumber, customerName, contactNumber, address, product, 1, pincode, null,"Not Updated Yet", "Not Updated Yet","no");
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
                            new Gson().toJson(docket.getProducts()), docket.getPincode());
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

    public static Map<String, String> getReasonCodeMap() {
        Map<String, String> reasonCodeMap = new HashMap<>();
        reasonCodeMap.put("101", "Can't Make It Work");
        reasonCodeMap.put("102", "Manufacturer Defect");
        reasonCodeMap.put("103", "Wrong Size");
        reasonCodeMap.put("104", "Wrong Item Delivered");
        reasonCodeMap.put("105", "Quality");
        reasonCodeMap.put("106", "Parts are missing");
        reasonCodeMap.put("107", "Wrong Style");
        reasonCodeMap.put("108", "Wrong Item Ordered");
        reasonCodeMap.put("109", "Only Product is damaged");
        reasonCodeMap.put("110", "Package and product both are damaged");
        reasonCodeMap.put("111", "Passed Expiry /Warranty Date");
        reasonCodeMap.put("112", "Authenticity");
        reasonCodeMap.put("113", "Wrong Color");
        reasonCodeMap.put("114", "Wrong Quantity");
        reasonCodeMap.put("115", "Null");
        return reasonCodeMap;
    }

    public static Map<Integer, Map<String, String>> getQcMatrix() {
        Map<Integer, Map<String, String>> qcMatrix = new HashMap<>();
        Map<String, String> valueMap = null;
        valueMap = new HashMap<>();
        valueMap.put("Product Description", "YES");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "YES");
        valueMap.put("Is Damaged", "YES");
        qcMatrix.put(101, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "YES");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "NO");
        valueMap.put("Is Damaged", "YES");
        qcMatrix.put(102, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "YES");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "YES");
        valueMap.put("Is Damaged", "YES");
        qcMatrix.put(103, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "NO");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "YES");
        valueMap.put("Is Damaged", "YES");
        qcMatrix.put(104, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "YES");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "NO");
        valueMap.put("Is Damaged", "YES");
        qcMatrix.put(105, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "YES");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "YES");
        valueMap.put("Is Damaged", "YES");
        qcMatrix.put(106, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "YES");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "YES");
        valueMap.put("Is Damaged", "YES");
        qcMatrix.put(107, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "YES");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "YES");
        valueMap.put("Is Damaged", "YES");
        qcMatrix.put(108, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "YES");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "YES");
        valueMap.put("Is Damaged", "NO");
        qcMatrix.put(109, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "YES");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "YES");
        valueMap.put("Is Damaged", "NO");
        qcMatrix.put(110, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "YES");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "YES");
        valueMap.put("Is Damaged", "YES");
        qcMatrix.put(111, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "YES");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "YES");
        valueMap.put("Is Damaged", "YES");
        qcMatrix.put(112, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "NO");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "YES");
        valueMap.put("Is Damaged", "YES");
        qcMatrix.put(113, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "YES");
        valueMap.put("Quantity", "NO");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "YES");
        valueMap.put("Clean/Not Used", "YES");
        valueMap.put("Is Damaged", "YES");
        qcMatrix.put(114, valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description", "YES");
        valueMap.put("Quantity", "YES");
        valueMap.put("Accessories in brand box", "YES");
        valueMap.put("Reason", "NO");
        valueMap.put("Clean/Not Used", "YES");
        valueMap.put("Is Damaged", "YES");
        qcMatrix.put(115, valueMap);

        return qcMatrix;
    }

    ;

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
