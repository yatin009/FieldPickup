package io.webguru.fieldpickup.Database;

import android.content.ContentValues;
import android.content.Context;
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
            MySQLiteHelper.COLUMN_PRODUCT_DESCRIPTION,
            MySQLiteHelper.COLUMN_IS_PENDING
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

    public Docket createDocket(String docketNumber, String customerName, String contact_number, String address, String productDescription,Integer isPending) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DOCKET_NUMBER, docketNumber);
        values.put(MySQLiteHelper.COLUMN_CUSTOMER_NAME, customerName);
        values.put(MySQLiteHelper.COLUMN_CONTACT_NUMBER, contact_number);
        values.put(MySQLiteHelper.COLUMN_ADDRESS, address);
        values.put(MySQLiteHelper.COLUMN_PRODUCT_DESCRIPTION, productDescription);
        values.put(MySQLiteHelper.COLUMN_IS_PENDING, isPending);
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

    public List<Docket> getAllDockets() {
        List<Docket> dockets = new ArrayList<>();

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

    private Docket cursorToDocket(Cursor cursor) {
        Docket docket = new Docket();
        docket.setId(cursor.getLong(0));
        docket.setDocketNumber(cursor.getString(1));
        docket.setCustomerName(cursor.getString(2));
        docket.setCustoumerContact(cursor.getString(3));
        docket.setCustoumerAddress(cursor.getString(4));
        docket.setDescription(cursor.getString(5));
        docket.setPending(cursor.getInt(6));
        return docket;
    }

}
