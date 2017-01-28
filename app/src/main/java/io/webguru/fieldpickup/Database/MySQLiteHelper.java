package io.webguru.fieldpickup.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mahto on 23/1/17.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_DOCKETS = "dockets";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DOCKET_NUMBER = "docket_number";
    public static final String COLUMN_CUSTOMER_NAME = "customer_name";
    public static final String COLUMN_CONTACT_NUMBER = "contact_number";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    public static final String COLUMN_IS_PENDING = "is_pending";

    private static final String DATABASE_NAME = "field_pickup";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String CREATE_TABLE_DOCKET = "create table "
            + TABLE_DOCKETS + "( " + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_DOCKET_NUMBER + " text not null,"
            + COLUMN_CUSTOMER_NAME + " text not null,"
            + COLUMN_CONTACT_NUMBER + " text not null,"
            + COLUMN_ADDRESS + " text not null,"
            + COLUMN_PRODUCT_DESCRIPTION + " text not null,"
            + COLUMN_IS_PENDING + " integer not null"
            + ");";

    public static final String TABLE_FIELD_DATA = "field_data";
    public static final String COLUMN_DOCKET_ID = "docket_id";
    public static final String COLUMN_FIELD_DATA_JSON = "field_data_json";


    private static final String CREATE_TABLE_FIELD_DATA = "create table "
            + TABLE_FIELD_DATA + "( " + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_DOCKET_ID + " inetger, "
            + COLUMN_FIELD_DATA_JSON + " text"
            + ");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_DOCKET);
        database.execSQL(CREATE_TABLE_FIELD_DATA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCKETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELD_DATA);
        onCreate(db);
    }

}
