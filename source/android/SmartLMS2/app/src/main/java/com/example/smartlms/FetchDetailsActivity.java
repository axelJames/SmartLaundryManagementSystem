package com.example.smartlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class FetchDetailsActivity extends AppCompatActivity {

    void changeStatus(int reqid, int newstat){
        Configs app = (Configs)getApplicationContext();
        String SERVER_IP =  app.getPrivData();
        final String URL = SERVER_IP + "washman/changestatus/";

        try {
            String  REQUEST_TAG = "com.example.smartlms.authRequest";
            SharedPreferences sharedPref = this.getSharedPreferences( "app_settings", Context.MODE_PRIVATE);
            String userid = sharedPref.getString("username","-1");
            String token = sharedPref.getString("token","-1");
            JSONObject lObj = new JSONObject();
            try {
                lObj.put("uname",userid);
                lObj.put("token",token);
                lObj.put("reqid",reqid);
                lObj.put("newstat",newstat);

            } catch (JSONException e) {
                e.printStackTrace();
            }
//        final String requestBody = AESEnc.encrypt(data);
            final String requestBody = lObj.toString();


            StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    String auth_response = response;
                    Toast toast = Toast.makeText(FetchDetailsActivity.this,
                            auth_response,
                            Toast.LENGTH_SHORT);

                    toast.show();

                    try {
                        JSONObject JSONobj = new JSONObject(auth_response);

                        if(JSONobj.getInt("success")==1){
                            Intent intent = new Intent(FetchDetailsActivity.this, ApproveActivity.class);
                            FetchDetailsActivity.this.startActivity(intent);
                        }
                        else {
                            Toast toast2 = Toast.makeText(FetchDetailsActivity.this,
                                    "Request failed",
                                    Toast.LENGTH_LONG);
                            toast2.show();
                        }



                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON:");
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast failed_toast = Toast.makeText(FetchDetailsActivity.this,
                            "Authentication Failed",
                            Toast.LENGTH_LONG);

//                            failed_toast.show();
                    VolleyLog.d(TAG, "Error: " + error.getMessage());

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

            RequestQSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void fetchDetails(final int reqid){

        Configs app = (Configs)getApplicationContext();
        String SERVER_IP =  app.getPrivData();
        final String URL = SERVER_IP + "washman/reqdetails/";

        try {
            String  REQUEST_TAG = "com.example.smartlms.authRequest";
            SharedPreferences sharedPref = this.getSharedPreferences( "app_settings", Context.MODE_PRIVATE);
            String userid = sharedPref.getString("username","-1");
            String token = sharedPref.getString("token","-1");
            JSONObject lObj = new JSONObject();
            try {
                lObj.put("uname",userid);
                lObj.put("token",token);
                lObj.put("reqid",reqid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//        final String requestBody = AESEnc.encrypt(data);
            final String requestBody = lObj.toString();


            StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    String auth_response = response;
                    Toast toast = Toast.makeText(FetchDetailsActivity.this,
                            auth_response,
                            Toast.LENGTH_SHORT);

                    toast.show();

                    try {
                        final JSONObject JSONobj = new JSONObject(auth_response);
                        TextView shirtsText = (TextView) findViewById(R.id.peShirts);
                        shirtsText.setText(Integer.toString(JSONobj.getInt("shirts")));
                        TextView pantsText = (TextView) findViewById(R.id.pePants);
                        pantsText.setText(Integer.toString(JSONobj.getInt("pants")));
                        TextView bedsheetssText = (TextView) findViewById(R.id.peBedSheets);
                        bedsheetssText.setText(Integer.toString(JSONobj.getInt("bedsheets")));
                        TextView innerwearsText = (TextView) findViewById(R.id.peUnderGrarments);
                        innerwearsText.setText(Integer.toString(JSONobj.getInt("shirts")));
                        TextView shortsText = (TextView) findViewById(R.id.peShorts);
                        shortsText.setText(Integer.toString(JSONobj.getInt("shorts")));
                        TextView tshirtsText = (TextView) findViewById(R.id.peTShirts);
                        tshirtsText.setText(Integer.toString(JSONobj.getInt("tshirts")));
                        TextView othersText = (TextView) findViewById(R.id.peOthers);
                        othersText.setText(Integer.toString(JSONobj.getInt("others")));

                        Button staBtn = (Button) findViewById(R.id.changestatus);
                        if(JSONobj.getInt("reqstat")==1) {
                            staBtn.setText("Notify");
                        }

                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON:");
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast failed_toast = Toast.makeText(FetchDetailsActivity.this,
                            "Authentication Failed",
                            Toast.LENGTH_LONG);

//                            failed_toast.show();
                    VolleyLog.d(TAG, "Error: " + error.getMessage());

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

            RequestQSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_details);
        final Integer reqid;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            reqid= -1;
        } else {
            reqid = extras.getInt("REQ_ID");
            fetchDetails(reqid);
        }
        final Button staBtn = (Button) findViewById(R.id.changestatus);

        staBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(staBtn.getText().toString().equals("Notify")){
                    changeStatus(reqid,3);
                }
                else {
                    changeStatus(reqid,1);
                }
            }
        });

        Button reject = (Button) findViewById(R.id.reject);
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus(reqid,2);
            }
        });

    }
}
