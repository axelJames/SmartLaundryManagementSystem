package com.example.smartlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class CompletedRequestActivity extends AppCompatActivity {

    void fillApprovedRequest(){

        Configs app = (Configs)getApplicationContext();
        String SERVER_IP =  app.getPrivData();
        final String AUTH_URL = SERVER_IP + "washman/approvedrequests/";
        // Do something in response to button click
//                SlmsRequests slmreq = new SlmsRequests(getApplicationContext());
        try {
            String  REQUEST_TAG = "com.example.smartlms.authRequest";
            SharedPreferences sharedPref = this.getSharedPreferences( "app_settings", Context.MODE_PRIVATE);
            String userid = sharedPref.getString("username","-1");
            String token = sharedPref.getString("token","-1");
            JSONObject lObj = new JSONObject();
            try {
                lObj.put("uname",userid);
                lObj.put("token",token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//        final String requestBody = AESEnc.encrypt(data);
            final String requestBody = lObj.toString();


            StringRequest strReq = new StringRequest(Request.Method.POST, AUTH_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    String auth_response = response;
                    Toast toast = Toast.makeText(CompletedRequestActivity.this,
                            auth_response,
                            Toast.LENGTH_SHORT);

                    toast.show();

                    try {
                        JSONObject JSONobj = new JSONObject(auth_response);
                        Integer status = JSONobj.getInt("status");
                        if (status==0){

                            ListView listView=(ListView)findViewById(R.id.pending_list);

                            final ArrayList<DataModel> dataModels= new ArrayList<>();
                            JSONArray reqarr = JSONobj.getJSONArray("requests");
                            JSONArray reqSts;

                            for (int i = 0; i < reqarr.length(); i++) {
                                reqSts = reqarr.getJSONArray(i);

                                dataModels.add(new DataModel(reqSts.getString(1),reqSts.getInt(0)));
                                Log.d("aaaaaaaaaaaaaaaaaaaaaa",reqSts.getString(1));
                            }

                            PendingRequestAdapter adapter= new PendingRequestAdapter(dataModels,getApplicationContext());

                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    DataModel dataModel= dataModels.get(position);
                                    Intent i = new Intent(CompletedRequestActivity.this, FetchDetailsActivity.class);
                                    Integer re_id = dataModel.getId();
                                    i.putExtra("REQ_ID", re_id);
                                    CompletedRequestActivity.this.startActivity(i);

                                }
                            });
                        }
                        else {
                            Toast failed_toast = Toast.makeText(CompletedRequestActivity.this,
                                    "Request Failed",
                                    Toast.LENGTH_LONG);

                            failed_toast.show();
                        }

                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON:");
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast failed_toast = Toast.makeText(CompletedRequestActivity.this,
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
        setContentView(R.layout.activity_pending_request);
        fillApprovedRequest();
    }

}
