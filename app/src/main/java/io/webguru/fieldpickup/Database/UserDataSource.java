package io.webguru.fieldpickup.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import io.webguru.fieldpickup.POJO.User;

/**
 * Created by panchanandmahto on 30/04/17.
 */

public class UserDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_USER_DETAILS
    };

    public UserDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public User getUserDetails() throws IOException {

        Cursor cursor = database.query(MySQLiteHelper.TABLE_USER,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        User user = cursorToUserDetails(cursor);
        cursor.close();
        return user;
    }



    public User insertUser(User user) throws IOException {
        if(user == null){
            return null;
        }
        ContentValues values = new ContentValues();
        ObjectMapper objectMapper = new ObjectMapper();
        String userDetailsJson = objectMapper.writeValueAsString(user);
        values.put(MySQLiteHelper.COLUMN_USER_DETAILS, userDetailsJson);
        long insertId = database.insert(MySQLiteHelper.TABLE_USER, null,values);
        return user;
    }

    private User cursorToUserDetails(Cursor cursor) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Long id = cursor.getLong(0);
        String userDetails = cursor.getString(1);
        User user = objectMapper.readValue(userDetails,User.class);
        return user;
    }
}
