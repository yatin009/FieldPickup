package io.webguru.fieldpickup.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.webguru.fieldpickup.POJO.FieldData;

//import static io.webguru.fieldpickup.R.id.quantity;

/**
 * Created by mahto on 24/1/17.
 */

public class FieldDataDataSource {


    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_DOCKET_ID,
            MySQLiteHelper.COLUMN_PRODUCT_ID,
            MySQLiteHelper.COLUMN_FIELD_DATA_JSON,
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

    public FieldData getFieldData(Long docketId) throws IOException {
        List<FieldData> fieldDatas = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from field_data where docket_id = ?", new String[] { docketId+"" });

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FieldData fieldData = cursorToFieldData(cursor);
            fieldDatas.add(fieldData);
            cursor.moveToNext();
        }
        cursor.close();
        return null;//fieldDatas == null || fieldDatas.isEmpty() ? null : fieldDatas.get(0);
    }

    public FieldData getFieldData(Long docketId, int productId) throws IOException {
        List<FieldData> fieldDatas = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from field_data where docket_id = ? and product_id = ?", new String[] { docketId+"", productId+"" });

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FieldData fieldData = cursorToFieldData(cursor);
            fieldDatas.add(fieldData);
            cursor.moveToNext();
        }
        cursor.close();
        return fieldDatas == null || fieldDatas.isEmpty() ? null : fieldDatas.get(0);
    }

    public FieldData insertFieldData(FieldData fieldData) throws IOException {
        if(fieldData == null){
            return null;
        }
        ContentValues values = new ContentValues();
        ObjectMapper objectMapper = new ObjectMapper();
        String fieldDataJson = objectMapper.writeValueAsString(fieldData);
        values.put(MySQLiteHelper.COLUMN_DOCKET_ID, fieldData.getDocketId());
        values.put(MySQLiteHelper.COLUMN_PRODUCT_ID, fieldData.getProductId());
        values.put(MySQLiteHelper.COLUMN_FIELD_DATA_JSON, fieldDataJson);
        long insertId = database.insert(MySQLiteHelper.TABLE_FIELD_DATA, null,values);
        fieldData.setId(insertId);
        return fieldData;
    }

    private FieldData cursorToFieldData(Cursor cursor) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Long id = cursor.getLong(0);
        String fieldDataJson = cursor.getString(3);
        FieldData fieldData = objectMapper.readValue(fieldDataJson,FieldData.class);
        fieldData.setId(id);
        return fieldData;
    }
}
