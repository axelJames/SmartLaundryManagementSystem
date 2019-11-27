package com.example.smartlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MenuActivity extends AppCompatActivity {
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
                            JSONArray reqStat = new JSONArray(response);
                            JSONObject reqSts;

                            for (int i = 0; i < reqStat.length(); i++) {
                                reqSts = reqStat.getJSONObject(i);
                                req_id = reqSts.getInt("reqid");
                                req_sta = reqSts.getInt("sta");
                                position = reqSts.getInt("position");
                                DBManager dbManager;

                                dbManager = new DBManager(MenuActivity.this);
                                dbManager.open();
                                dbManager.update(req_id,req_sta,position);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(
                            VolleyError error) {


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

        RequestQSingleton.getInstance(MenuActivity.this).addToRequestQueue(request, REQUEST_TAG);
        return true;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button btnHist = (Button) findViewById(R.id.HistoryBtn);
        btnHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendRequest();
                Intent intent = new Intent(MenuActivity.this, HistoryActivity.class);
                MenuActivity.this.startActivity(intent);
            }
        });

        Button btnBack = (Button) findViewById(R.id.button4);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, HomeActivity.class);
                MenuActivity.this.startActivity(intent);
            }
        });
    }
}
