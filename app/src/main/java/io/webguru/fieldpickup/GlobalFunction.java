package io.webguru.fieldpickup;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.webguru.fieldpickup.Activities.MainActivity;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.Fragments.BlankFragment;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;
import io.webguru.fieldpickup.POJO.Tab;

/**
 * Created by yatin on 21/01/17.
 */

public class GlobalFunction {

    private static DocketDataSource docketDataSource;
    private static FieldDataDataSource fieldDataDataSource;


    private static String[] docketNumberList = {"RP-415221", "RP-415255", "RP-415223", "RP-415276", "RP-415288", "RP-415232", "RP-415333", "RP-415365", "RP-415612", "RP-415543"};

    private static String[] customerNameList = {"Yogesh sharma", "Dr. Ashwani Kumar Gupta", "MANU SHARMA", "Abhishek pal", "puneet vashistha", "Neeraj Joshi", "Dr. Gautam Bhattacharya", "Shweta Verma", "manish dhar", "sunil kr sharma"};

    private static String[] contactNumberList = {"+91-9012547691", "7042067740", "9769555880", "8953903078", "9717394845", "9810214312", "9899154267", "8287356604", "9810345061", "9775555430"};


    private static String[] productDescriptionList = {"Intex Fun Swimming Pool - 6 Feet", "Shiv Shakti Brown Classic Wall Clock", "Kitchen pro 42 pcs Dinner Set With Serving Spoons",
            "Asian Red Sports Shoes (Size: 32 EU)", "Asian Black School Shoes for Boys (Size: 37 EU)", "Kalinga Gold Cu. Flex. Blue Wire - 1.5 Mm",
            "Total Home Appliances - Dough Maker atta Maker aata Maker With Manual Blender", "Shagun Green Plastic Basket Mop With 4 Extra Refills",
            "Dhoom Soft Toys Teddy Bear Jumbo 5 Feet Cream- 60inches", "V-Guard VMB400 Voltage Stabilizer (for AC Upto 1.5 Ton)"};

    private static String[] addressList = {"IEX LtD 4th Floor TDI Tower Jasola District Center, Near Apolo Hospital, New Delhi Delhi",
            "H 13 Kasturba Apartment Pitampura, Behind Bhagwan Mahavir Hospital., New Delhi Delhi",
            "644 Pocket E Mayur Vihar Phase 2 East Delhi, Pocket E, New Delhi Delhi",
            "R3A2-108 mohan garden uttam nagar new delhi-59, Chandi farm, New Delhi Delhi",
            "Vpo amberhai sec19 dwarka HOUSE NO. 68, Near panchayat ghar, New Delhi Delhi",
            "House No. 154 Dayanand Vihar, Near Karkardooma Metro Station, New Delhi Delhi",
            "HOUSE NO-3, GAYANODAYA SECONDARY PUBLIC SCHOOL DINPUR NAJAFGARH, NEW DELHI Delhi",
            "D 7/9 SF Exclusive Floors DLF City Phase V, DLF City Phase V, Gurgaon Haryana",
            "Duplex Bungalow No 7 Type VI M.A.M.C Campus, -, New Delhi Delhi",
            "IEX LtD 4th Floor TDI Tower Jasola District Center, Near Apolo Hospital, New Delhi Delhi"};


    public static ArrayList<Docket> getDocketList(Integer isPending, Context context) {
        ArrayList<Docket> dockets = new ArrayList<>();
        Docket docket = null;
        Map<String, Docket> insertedDocketNumberMap = new HashMap<>();
        docketDataSource = new DocketDataSource(context);
        docketDataSource.open();
        List<Docket> docketList = docketDataSource.getAllDockets();
        if (docketList != null && !docketList.isEmpty()) {
            for (Docket docket1 : docketList) {
                insertedDocketNumberMap.put(docket1.getDocketNumber(), docket1);
            }
        }
        for (int i = 0; i < docketNumberList.length; i++) {
            if (insertedDocketNumberMap.containsKey(docketNumberList[i])) {
                if (insertedDocketNumberMap.get(docketNumberList[i]).isPending() == 1) {
                    dockets.add(insertedDocketNumberMap.get(docketNumberList[i]));
                }
            } else {
                docket = docketDataSource.createDocket(docketNumberList[i], customerNameList[i], contactNumberList[i], addressList[i], productDescriptionList[i], 1);
                dockets.add(docket);
            }
        }
        return dockets;
    }

    public static ArrayList<Docket> getDoneDocketList() {
        ArrayList<Docket> dockets = new ArrayList<>();
        List<Docket> docketList = docketDataSource.getAllDockets();
        if (docketList != null && !docketList.isEmpty()) {
            for (Docket docket : docketList) {
                if (docket.isPending() == 0) {
                    dockets.add(docket);
                }
            }
        }
        return dockets;
    }

    public static FieldData getFieldData(Long docketId, Context context) throws IOException {
        fieldDataDataSource = new FieldDataDataSource(context);
        fieldDataDataSource.open();
        FieldData fieldData = fieldDataDataSource.getFieldData(docketId);
        fieldDataDataSource.close();
        return fieldData;
    }


    public static void showNotification(Context context, Intent intent, String message) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
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

}
