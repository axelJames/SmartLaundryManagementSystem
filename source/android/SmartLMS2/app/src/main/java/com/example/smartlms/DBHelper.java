package com.example.smartlms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "REQUESTS";

    public static final String _ID = "_id";
    public static final String SHIRTS = "shirts";
    public static final String PANTS = "pants";
    public static final String SHORTS = "shorts";
    public static final String TOWELS = "towels";
    public static final String BEDSHEETS = "bed_sheets";
    public static final String INNERWEAR = "innerwear";
    public static final String TSHIRTS = "tshirts";
    public static final String OTHERS = "others";
    public static final String POSITION = "position";
    public static final String REQUESTTIME = "requesttime";
    public static final String STATUS = "status";

    public static final String TABLE2_NAME = "IMAGES";

    public static final String REQUEST_ID = "request_id";
    public static final String IMAGE_PATH = "image_path";

    // Database Information
    static final String DB_NAME = "LAUNDRY_MANAGEMENT.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE_REQUESTS = "create table " + TABLE_NAME + "("
            + _ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SHIRTS + " INTEGER NOT NULL, "
            + PANTS + " INTEGER NOT NULL, "
            + SHORTS + " INTEGER NOT NULL, "
            + TOWELS + " INTEGER NOT NULL, "
            + BEDSHEETS + " INTEGER NOT NULL, "
            + INNERWEAR + " INTEGER NOT NULL, "
            + TSHIRTS + " INTEGER NOT NULL, "
            + OTHERS + " INTEGER NOT NULL, "
            + STATUS + " INTEGER, "
            + POSITION + " INTEGER, "
            + REQUESTTIME + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    private static final String CREATE_TABLE_IMAGES = "create table " + TABLE2_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REQUEST_ID + " INTEGER NOT NULL, " + IMAGE_PATH + " TEXT);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_REQUESTS);
        db.execSQL(CREATE_TABLE_IMAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
        onCreate(db);
    }
}