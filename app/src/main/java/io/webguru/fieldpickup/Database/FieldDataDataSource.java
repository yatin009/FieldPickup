package io.webguru.fieldpickup.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.webguru.fieldpickup.POJO.FieldData;

/**
 * Created by mahto on 24/1/17.
 */

public class FieldDataDataSource {


    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_IS_SAME_PRODUCT_RECEIVED,
            MySQLiteHelper.COLUMN_QUANTITY,
            MySQLiteHelper.COLUMN_IS_ALL_PARTS_AVAILABLE,
            MySQLiteHelper.COLUMN_IS_ISSUE_CATEGORY_CORRECT,
            MySQLiteHelper.COLUMN_IS_PRODUCT_DIRTY,
            MySQLiteHelper.COLUMN_AGENT_REMARKS,
            MySQLiteHelper.COLUMN_DOCKET_ID
    };

    public FieldDataDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public FieldData createFieldData(boolean isSameProductReceived, Integer quantity, boolean isAllPartsAvailable, boolean isIssueCategoryCorrect, boolean isProductDirty, String agentRemarks, Long docketId) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_IS_SAME_PRODUCT_RECEIVED, isSameProductReceived ? 1 : 0);
        values.put(MySQLiteHelper.COLUMN_QUANTITY, quantity);
        values.put(MySQLiteHelper.COLUMN_IS_ALL_PARTS_AVAILABLE, isAllPartsAvailable ? 1 : 0);
        values.put(MySQLiteHelper.COLUMN_IS_ISSUE_CATEGORY_CORRECT, isIssueCategoryCorrect ? 1 : 0);
        values.put(MySQLiteHelper.COLUMN_IS_PRODUCT_DIRTY, isProductDirty ? 1 : 0);
        values.put(MySQLiteHelper.COLUMN_AGENT_REMARKS, agentRemarks);
        values.put(MySQLiteHelper.COLUMN_DOCKET_ID, docketId);

        long insertId = database.insert(MySQLiteHelper.TABLE_FIELD_DATA, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_FIELD_DATA,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        FieldData newFieldData = cursorToFieldData(cursor);
        cursor.close();
        return newFieldData;
    }

    public FieldData getFieldData(Long docketId) {
        List<FieldData> fieldDatas = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from field_data where docket_id = ?", new String[] { docketId+"" });

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FieldData fieldData = cursorToFieldData(cursor);
            fieldDatas.add(fieldData);
            cursor.moveToNext();
        }
        cursor.close();
        return fieldDatas == null || fieldDatas.isEmpty() ? null : fieldDatas.get(0);
    }

    public FieldData insertFieldData(Long docketId,boolean isSameProductReceived, int quantity, boolean isAllPartsAvailable,
                                     boolean isIssueCategoryCorrect, boolean isProductDirty, String agentRemarks) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DOCKET_ID, docketId);
        values.put(MySQLiteHelper.COLUMN_IS_SAME_PRODUCT_RECEIVED, isSameProductReceived ? 1 : 0);
        values.put(MySQLiteHelper.COLUMN_QUANTITY, quantity);
        values.put(MySQLiteHelper.COLUMN_IS_ALL_PARTS_AVAILABLE, isAllPartsAvailable ? 1 : 0);
        values.put(MySQLiteHelper.COLUMN_IS_ISSUE_CATEGORY_CORRECT, isIssueCategoryCorrect ? 1 : 0);
        values.put(MySQLiteHelper.COLUMN_IS_PRODUCT_DIRTY, isProductDirty ? 1 : 0);
        values.put(MySQLiteHelper.COLUMN_AGENT_REMARKS, agentRemarks);

        long insertId = database.insert(MySQLiteHelper.TABLE_FIELD_DATA, null,values);
        FieldData fieldData = new FieldData(isSameProductReceived,quantity,isAllPartsAvailable,isIssueCategoryCorrect,isProductDirty,agentRemarks,docketId);
        fieldData.setId(insertId);

        return fieldData;
    }

    private FieldData cursorToFieldData(Cursor cursor) {
        FieldData fieldData = new FieldData();
        fieldData.setId(cursor.getLong(0));
        fieldData.setSameProduct(cursor.getInt(1) == 1 ? true : false);
        fieldData.setQuantity(cursor.getInt(2));
        fieldData.setAllPartsAvailable(cursor.getInt(3) == 1 ? true : false);
        fieldData.setIssueCategoryCorrect(cursor.getInt(4) == 1 ? true : false);
        fieldData.setProductDirty(cursor.getInt(5) == 1 ? true : false);
        fieldData.setAgentRemarks(cursor.getString(6));
        return fieldData;
    }
}
