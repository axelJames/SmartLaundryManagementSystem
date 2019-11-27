package com.example.smartlms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class DBManager {

    private DBHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert_request(Integer id, Integer shirts, Integer pants, Integer shorts, Integer towels, Integer bedsheets, Integer innerwear, Integer tshirts, Integer others, Integer status, Integer position) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper._ID, id);
        contentValue.put(DBHelper.SHIRTS, shirts);
        contentValue.put(DBHelper.PANTS, pants);
        contentValue.put(DBHelper.SHORTS, shorts);
        contentValue.put(DBHelper.TOWELS, towels);
        contentValue.put(DBHelper.BEDSHEETS, bedsheets);
        contentValue.put(DBHelper.INNERWEAR, innerwear);
        contentValue.put(DBHelper.TSHIRTS, tshirts);
        contentValue.put(DBHelper.OTHERS, others);
        contentValue.put(DBHelper.STATUS, status);
        contentValue.put(DBHelper.POSITION, position);
        long i=database.insert(DBHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DBHelper._ID, DBHelper.STATUS, DBHelper.POSITION, DBHelper.REQUESTTIME };
        Cursor cursor = database.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(Integer _id, Integer status, Integer position) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.STATUS, status);
        contentValues.put(DBHelper.POSITION, position);
        int i = database.update(DBHelper.TABLE_NAME, contentValues, DBHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DBHelper.TABLE_NAME, DBHelper._ID + "=" + _id, null);
    }

    public Cursor getDetails(Integer req_id) {
//        String[] columns = new String[] { DBHelper._ID, DBHelper.SHIRTS, DBHelper.P,DBHelper,DBHelper,DBHelper,DBHelper. ,DBHelper.STATUS, DBHelper.REQUESTTIME };
        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, DBHelper._ID + " = " + req_id, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public JSONArray getPendingRequests() {
        JSONArray mArrayList = new JSONArray();
        String[] columns = new String[] { DBHelper._ID };
        Cursor cursor = database.query(DBHelper.TABLE_NAME, columns, DBHelper.STATUS + " = 0 OR "+ DBHelper.STATUS + " = 1 " , null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                mArrayList.put(cursor.getInt(cursor.getColumnIndex(DBHelper._ID))); //add the item
                cursor.moveToNext();
            }
        }

        return  mArrayList;
    }
}

