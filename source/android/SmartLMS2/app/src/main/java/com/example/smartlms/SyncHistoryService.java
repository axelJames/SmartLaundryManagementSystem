package com.example.smartlms;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SyncHistoryService extends Service{

    private static final String TAG="SyncService";
    private boolean isRunning=false;
    private IBinder mBinder=new MyBinder();
    private boolean intenetAccess=false;


    public boolean SendRequest()
    {
        String  REQUEST_TAG = "com.example.smartlms.syncRequest";
        Configs app = (Configs)getApplicationContext();
        String SERVER_IP =  app.getPrivData();
        String SYNC_URL = SERVER_IP + "laundry/status/";
        SharedPreferences sharedPref = this.getSharedPreferences( "app_settings", Context.MODE_PRIVATE);
        String userid = sharedPref.getString("username","-1");
        String token = sharedPref.getString("token","-1");

        DBManager dbManager;

        dbManager = new DBManager(this);
        dbManager.open();
        JSONArray reqIds = dbManager.getPendingRequests();

        JSONObject lObj = new JSONObject();
        try {
            lObj.put("uname",userid);
            lObj.put("token",token);
            lObj.put("reqids", reqIds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        final String requestBody = AESEnc.encrypt(data);
        final String requestBody = lObj.toString();

        StringRequest request=new StringRequest(com.android.volley.Request.Method.POST,
                SYNC_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(
                            String response) {
                        try {
                            int req_id, req_sta, position;
                            JSONObject JSONStatus = new JSONObject(response);
                            JSONObject reqSts;
                            JSONArray reqStat = JSONStatus.getJSONArray("reqstat");
                            for (int i = 0; i < reqStat.length(); i++) {
                                reqSts = reqStat.getJSONObject(i);
                                req_id = reqSts.getInt("reqid");
                                req_sta = reqSts.getInt("sta");
                                position = reqSts.getInt("position");
                                DBManager dbManager;

                                dbManager = new DBManager(SyncHistoryService.this);
                                dbManager.open();
                                dbManager.update(req_id,req_sta, position);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        intenetAccess=true;
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(
                            VolleyError error) {

                        intenetAccess=false;

                    }
                })
            {
                @Override
                public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

                @Override
                public byte[] getBody() {

                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding");
                    return null;
                }
            }
            };

        RequestQSingleton.getInstance(SyncHistoryService.this).addToRequestQueue(request, REQUEST_TAG);
        return intenetAccess;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service onCreate");

        isRunning=true;

    }



    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Service onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "Service onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Service onUnBind");
        return true;
    }

    @Override
    public void onDestroy() {

        isRunning=false;

        Log.i(TAG, "Service onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SendRequest();
        return Service.START_NOT_STICKY;
    }

    public class MyBinder extends Binder
    {
        SyncHistoryService getService()
        {
            return SyncHistoryService.this;
        }
    }
}