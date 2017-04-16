package io.webguru.fieldpickup.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.webguru.fieldpickup.POJO.Docket;

/**
 * Created by mahto on 23/1/17.
 */

public class DocketDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_DOCKET_NUMBER,
            MySQLiteHelper.COLUMN_CUSTOMER_NAME,
            MySQLiteHelper.COLUMN_CONTACT_NUMBER,
            MySQLiteHelper.COLUMN_ADDRESS,
            MySQLiteHelper.COLUMN_PRODUCTS,
            MySQLiteHelper.COLUMN_IS_PENDING,
            MySQLiteHelper.COLUMN_PINCODE,
            MySQLiteHelper.COLUMN_ORDER_NUMBER,
            MySQLiteHelper.COLUMN_IS_SYNCED,
    };

    public DocketDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Docket createDocket(String docketNumber, String customerName, String contact_number, String address,
                               String products, Integer isPending, String pincode, String orderNumber) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DOCKET_NUMBER, docketNumber);
        values.put(MySQLiteHelper.COLUMN_CUSTOMER_NAME, customerName);
        values.put(MySQLiteHelper.COLUMN_CONTACT_NUMBER, contact_number);
        values.put(MySQLiteHelper.COLUMN_ADDRESS, address);
        values.put(MySQLiteHelper.COLUMN_PRODUCTS, products);
        values.put(MySQLiteHelper.COLUMN_IS_PENDING, isPending);
        values.put(MySQLiteHelper.COLUMN_PINCODE, pincode);
        values.put(MySQLiteHelper.COLUMN_ORDER_NUMBER, orderNumber);
        values.put(MySQLiteHelper.COLUMN_IS_SYNCED, 0);

        long insertId = database.insert(MySQLiteHelper.TABLE_DOCKETS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DOCKETS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Docket newDocket = cursorToDocket(cursor);
        cursor.close();
        return newDocket;
    }

    public ArrayList<Docket> getAllDockets() {
        ArrayList<Docket> dockets = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_DOCKETS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Docket docket = cursorToDocket(cursor);
            dockets.add(docket);
            cursor.moveToNext();
        }
        cursor.close();
        return dockets;
    }

    public Docket getDocket(Long docketId) {

        Docket docket = null;
        Cursor cursor = database.rawQuery("select * from dockets where _id = ?", new String[] { docketId+"" });


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            docket = cursorToDocket(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return docket;
    }

    public Docket markDocketAsDone(Long docketId) {

        Docket docket = null;
        Cursor cursor = database.rawQuery("update dockets set is_pending = 0 where _id = ?", new String[] { docketId+"" });


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            docket = cursorToDocket(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return docket;
    }

    public void updateDocket(Docket docket){
        ContentValues cv = new ContentValues();
        cv.put(MySQLiteHelper.COLUMN_PRODUCTS, docket.getProductsStringJson());
        cv.put(MySQLiteHelper.COLUMN_IS_PENDING, 0);
        database.update(MySQLiteHelper.TABLE_DOCKETS, cv, MySQLiteHelper.COLUMN_ID + "= ?", new String[] {docket.getId()+""});
    }

    public int markDocketsAsSynced(List<Long> docketIdList) {

        if(docketIdList == null || docketIdList.isEmpty()){
            return 0;
        }
        String str = "";
        for(Long id : docketIdList){
            if(!str.equals("")){
                str += ",";
            }
            str +=id;
        }

        Cursor cursor = database.rawQuery("update dockets set is_synced = 1 where _id in (" + str + ")", null);

        int count = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            count++;
            cursor.moveToNext();
        }
        cursor.close();
        return count;
    }

    private Docket cursorToDocket(Cursor cursor) {
        Docket docket = new Docket();
        docket.setId(cursor.getLong(0));
        docket.setAwbNumber(cursor.getString(1));
        docket.setCustomerName(cursor.getString(2));
        docket.setCustomerContact(cursor.getString(3));
        docket.setCustomerAddress(cursor.getString(4));
        docket.setProducts(cursor.getString(5));
        docket.setPending(cursor.getInt(6));
        docket.setPincode(cursor.getString(7));
        docket.setOrderNumber(cursor.getString(8));
        docket.setIsSynced(cursor.getInt(9));
        return docket;
    }

}
