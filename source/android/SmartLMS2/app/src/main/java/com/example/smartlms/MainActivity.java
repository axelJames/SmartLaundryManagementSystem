package com.example.smartlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.android.volley.VolleyLog.TAG;

public class MainActivity extends AppCompatActivity {

    void authRequest(final String userid, String password, final Boolean stud){

        Configs app = (Configs)getApplicationContext();
        String SERVER_IP =  app.getPrivData();
        final String AUTH_URL;
        if (stud)
            AUTH_URL = SERVER_IP + "accounts/login/";
        else
            AUTH_URL = SERVER_IP + "washman/login/";
        // Do something in response to button click
//                SlmsRequests slmreq = new SlmsRequests(getApplicationContext());
        try {
            String  REQUEST_TAG = "com.example.smartlms.authRequest";
            JSONObject auth_request  = new JSONObject();
            auth_request.put("uname",userid);
            auth_request.put("password",password);
            String data = auth_request.toString();
//        final String requestBody = AESEnc.encrypt(data);
            final String requestBody = data;
//                    final Context mcontext = getApplicationContext();

            StringRequest strReq = new StringRequest(Request.Method.POST, AUTH_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    String auth_response = response;
                    Toast toast = Toast.makeText(MainActivity.this,
                            auth_response,
                            Toast.LENGTH_SHORT);

                    toast.show();

                    try {
                        JSONObject JSONobj = new JSONObject(auth_response);
                        Integer status = JSONobj.getInt("status");
                        if (status==0){

                            if (stud) {
                                String token = JSONobj.getString("token");
                                Integer hnum = JSONobj.getInt("hnum");
                                Integer load = JSONobj.getInt("load");
                                SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("app_settings", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("username", userid);
                                editor.putString("token", token);
                                editor.putInt("hostel", hnum);
                                editor.putInt("load", load);
                                editor.putInt("type", 0);
                                editor.commit();

                                startService(new Intent(MainActivity.this, SyncHistoryService.class));

                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                MainActivity.this.startActivity(intent);
                            }
                            else {
                                String token = JSONobj.getString("token");
                                SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("app_settings", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("username", userid);
                                editor.putString("token", token);
                                editor.putInt("type", 1);
                                editor.commit();
                                Intent intent = new Intent(MainActivity.this, ApproveActivity.class);
                                MainActivity.this.startActivity(intent);
                            }
                        }
                        else {
                            Toast failed_toast = Toast.makeText(MainActivity.this,
                                    "Authentication Failed",
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
                    Toast failed_toast = Toast.makeText(MainActivity.this,
                            "Authentication Failed",
                            Toast.LENGTH_LONG);

//                            failed_toast.show();
                    VolleyLog.d(TAG, "Error: " + error.getMessage());

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(MainActivity.this,
                                "No network",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        //TODO
                        Toast.makeText(MainActivity.this,
                                "E2",
                                Toast.LENGTH_LONG).show();

                    } else if (error instanceof ServerError) {
                        //TODO
                        Toast.makeText(MainActivity.this,
                                "E3",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        //TODO
                        Toast.makeText(MainActivity.this,
                                "E4",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        //TODO
                        Toast.makeText(MainActivity.this,
                                "E5",
                                Toast.LENGTH_LONG).show();
                    }
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
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getSharedPreferences( "app_settings", Context.MODE_PRIVATE);
        String userid = sharedPref.getString("username","-1");
        String token = sharedPref.getString("token","-1");
        if(userid!="-1"&&token!="-1"){
            if (sharedPref.getInt("type", 0)==1){
                Intent intent = new Intent(MainActivity.this, ApproveActivity.class);
                MainActivity.this.startActivity(intent);
            }
            else {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                MainActivity.this.startActivity(intent);
            }
        }
//        Button button = (Button) findViewById(R.id.LdapLoginBtn);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                authRequest(userid,password);
//            }
//        });

        final EditText unameText = (EditText) findViewById(R.id.input_id);

        final EditText passText = (EditText) findViewById(R.id.input_password);
        Button btn = (Button) findViewById(R.id.LdapLoginBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid=true;
                String userid = unameText.getText().toString();
                String password = passText.getText().toString();
                if (userid.isEmpty()) {
                    unameText.setError("enter a valid id");
                    valid = false;
                } else {
                    unameText.setError(null);
                }

                if (password.isEmpty()) {
                    passText.setError("between 4 and 10 alphanumeric characters");
                    valid = false;
                } else {
                    passText.setError(null);
                }
                if (valid) {
                    Configs app = (Configs)getApplicationContext();
                    String staffID =  app.getStaffID();
                    String staffPass = app.getStaffPass();
                    authRequest(userid, password, true);
                }
            }
        });

        Button btn2 = (Button) findViewById(R.id.StaffLoginBtn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid=true;
                String userid = unameText.getText().toString();
                String password = passText.getText().toString();
                if (userid.isEmpty()) {
                    unameText.setError("enter a valid id");
                    valid = false;
                } else {
                    unameText.setError(null);
                }

                if (password.isEmpty()) {
                    passText.setError("between 4 and 10 alphanumeric characters");
                    valid = false;
                } else {
                    passText.setError(null);
                }
                if (valid) {
                    Configs app = (Configs)getApplicationContext();
                    String staffID =  app.getStaffID();
                    String staffPass = app.getStaffPass();
                    authRequest(userid, password, false);
                }
            }
        });

    }
}
