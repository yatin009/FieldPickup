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
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;
import io.webguru.fieldpickup.POJO.Question;

/**
 * Created by yatin on 21/01/17.
 */

public class GlobalFunction {

    private static DocketDataSource docketDataSource;
    private static FieldDataDataSource fieldDataDataSource;

    private static String[] docketNumberList = {"RP-415221", "RP-415255", "RP-415223", "RP-415276", "RP-415288", "RP-415232", "RP-415333", "RP-415365", "RP-415612", "RP-415543"};
    private static String[] orderNumberList = {"886475", "874322", "998382", "445733", "654432", "784132", "834526", "934287", "869076", "188734"};

    private static String[] customerNameList = {"Yogesh sharma", "Dr. Ashwani Kumar Gupta", "MANU SHARMA", "Abhishek pal", "puneet vashistha", "Neeraj Joshi", "Dr. Gautam Bhattacharya", "Shweta Verma", "manish dhar", "sunil kr sharma"};

    private static String[] contactNumberList = {"+91-9012547691", "7042067740", "9769555880", "8953903078", "9717394845", "9810214312", "9899154267", "8287356604", "9810345061", "9775555430"};
    private static String[] pincodeList = {"110020", "110092", "110045", "110056", "110023", "201301", "110054", "110020", "201301", "110023"};
    private static String[] reasonList = {"103", "105", "102", "106", "105", "104", "113", "109", "110", "114"};

    private static Integer[] quantityList = {1, 2, 1, 3, 2, 4, 1, 2, 1, 3};


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


    private static Question question1 = new Question("Same product received ?","RADIO",null,null);
    private static Question question2 = new Question("Picked quantity","NUMBER_DROPDOWN",null,null);
    private static Question question3 = new Question("All accessories/parts available ?","RADIO",null,null);
    private static Question question4 = new Question("Issue Category is Correct ?","RADIO",null,null);
    private static Question question5 = new Question("Product is dirty/used ?","RADIO",null,null);
    private static Question question6 = new Question("Remarks","TEXT",null,null);

    private static List<Question> questionList = new ArrayList<>();

    public static List<Question> getQuestionList(){
        questionList.add(question1);
        questionList.add(question2);
        questionList.add(question3);
        questionList.add(question4);
        questionList.add(question5);
        questionList.add(question6);
        return questionList;
    }


    public static ArrayList<Docket> getDocketList(Integer isPending, Context context) {
        ArrayList<Docket> dockets = new ArrayList<>();
        Docket docket = null;
        Map<String, Docket> insertedDocketNumberMap = new HashMap<>();
        docketDataSource = new DocketDataSource(context);
        docketDataSource.open();
        List<Docket> docketList = docketDataSource.getAllDockets();
        if (docketList != null && !docketList.isEmpty()) {
            for (Docket docket1 : docketList) {
                insertedDocketNumberMap.put(docket1.getAwbNumber(), docket1);
            }
        }
        for (int i = 0; i < docketNumberList.length; i++) {
            if (insertedDocketNumberMap.containsKey(docketNumberList[i])) {
                if (insertedDocketNumberMap.get(docketNumberList[i]).isPending() == 1) {
                    dockets.add(insertedDocketNumberMap.get(docketNumberList[i]));
                }
            } else {
                docket = docketDataSource.createDocket(docketNumberList[i], customerNameList[i],
                        contactNumberList[i], addressList[i], productDescriptionList[i], 1,
                        reasonList[i],pincodeList[i],quantityList[i], orderNumberList[i]);
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

    public static Map<String, String> getReasonCodeMap(){
        Map<String,String> reasonCodeMap = new HashMap<>();
        reasonCodeMap.put("101","Can't Make It Work");
        reasonCodeMap.put("102","Manufacturer Defect");
        reasonCodeMap.put("103","Wrong Size");
        reasonCodeMap.put("104","Wrong Item Delivered");
        reasonCodeMap.put("105","Quality");
        reasonCodeMap.put("106","Parts are missing");
        reasonCodeMap.put("107","Wrong Style");
        reasonCodeMap.put("108","Wrong Item Ordered");
        reasonCodeMap.put("109","Only Product is damaged");
        reasonCodeMap.put("110","Package and product both are damaged");
        reasonCodeMap.put("111","Passed Expiry /Warranty Date");
        reasonCodeMap.put("112","Authenticity");
        reasonCodeMap.put("113","Wrong Color");
        reasonCodeMap.put("114","Wrong Quantity");
        reasonCodeMap.put("115","Null");
        return reasonCodeMap;
    }

    public static Map<Integer,Map<String,String>> getQcMatrix(){
        Map<Integer,Map<String,String>> qcMatrix = new HashMap<>();
        Map<String, String> valueMap = null;
        valueMap = new HashMap<>();
        valueMap.put("Product Description","YES");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","YES");
        valueMap.put("Is Damaged","YES");
        qcMatrix.put(101,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","YES");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","NO");
        valueMap.put("Is Damaged","YES");
        qcMatrix.put(102,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","YES");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","YES");
        valueMap.put("Is Damaged","YES");
        qcMatrix.put(103,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","NO");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","YES");
        valueMap.put("Is Damaged","YES");
        qcMatrix.put(104,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","YES");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","NO");
        valueMap.put("Is Damaged","YES");
        qcMatrix.put(105,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","YES");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","YES");
        valueMap.put("Is Damaged","YES");
        qcMatrix.put(106,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","YES");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","YES");
        valueMap.put("Is Damaged","YES");
        qcMatrix.put(107,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","YES");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","YES");
        valueMap.put("Is Damaged","YES");
        qcMatrix.put(108,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","YES");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","YES");
        valueMap.put("Is Damaged","NO");
        qcMatrix.put(109,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","YES");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","YES");
        valueMap.put("Is Damaged","NO");
        qcMatrix.put(110,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","YES");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","YES");
        valueMap.put("Is Damaged","YES");
        qcMatrix.put(111,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","YES");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","YES");
        valueMap.put("Is Damaged","YES");
        qcMatrix.put(112,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","NO");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","YES");
        valueMap.put("Is Damaged","YES");
        qcMatrix.put(113,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","YES");
        valueMap.put("Quantity","NO");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","YES");
        valueMap.put("Clean/Not Used","YES");
        valueMap.put("Is Damaged","YES");
        qcMatrix.put(114,valueMap);

        valueMap = new HashMap<>();
        valueMap.put("Product Description","YES");
        valueMap.put("Quantity","YES");
        valueMap.put("Accessories in brand box","YES");
        valueMap.put("Reason","NO");
        valueMap.put("Clean/Not Used","YES");
        valueMap.put("Is Damaged","YES");
        qcMatrix.put(115,valueMap);

        return qcMatrix;
    };

    public boolean qualityCheck(){

        return true;
    }

}
