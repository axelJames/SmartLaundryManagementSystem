package com.example.smartlms;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.android.volley.VolleyLog.TAG;

public class NewRequestActivity extends AppCompatActivity {

    public void laundryRequest(final JSONObject lRequest) throws Exception {

        Configs app = (Configs)getApplicationContext();
        String SERVER_IP =  app.getPrivData();
        String NEW_REQUEST_URL = SERVER_IP + "laundry/order/";

        String  REQUEST_TAG = "com.example.smartlms.newRequest";
        SharedPreferences sharedPref = NewRequestActivity.this.getSharedPreferences( "app_settings", Context.MODE_PRIVATE);
        String userid = sharedPref.getString("username","-1");
        String token = sharedPref.getString("token","-1");
        lRequest.put("uname",userid);
        lRequest.put("token",token);
        String data = lRequest.toString();
//        final String requestBody = AESEnc.encrypt(data);
        final String requestBody = data;
//        final Context mcontext = this.context;

        StringRequest strReq = new StringRequest(Request.Method.POST, NEW_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                String new_req_response = response;
                Toast toast = Toast.makeText(NewRequestActivity.this,
                        new_req_response,
                        Toast.LENGTH_SHORT);

                toast.show();

                JSONObject JSONobj = null;
                try {
                    JSONobj = new JSONObject(new_req_response);
                    Integer status = JSONobj.getInt("reqstat");
                    if (status!=4){
                        Integer reqid = JSONobj.getInt("reqid");
                        DBManager db = new DBManager(NewRequestActivity.this);
                        db.open();
                        db.insert_request(reqid,lRequest.getInt("shirts"),
                                lRequest.getInt("pants"),
                                lRequest.getInt("shorts"),
                                lRequest.getInt("towels"),
                                lRequest.getInt("bedsheets"),
                                lRequest.getInt("innerwears"),
                                lRequest.getInt("tshirts"),
                                lRequest.getInt("others"),
                                status,-1);

                        Intent intent = new Intent(NewRequestActivity.this, RequestSuccessActivity.class);
                        Bundle extras = intent.getExtras();
//                        extras.putInt("QueueNumber", JSONobj.getInt("position"));
                        NewRequestActivity.this.startActivity(intent);

                    }
                    else {
                        Toast failed_toast = Toast.makeText(NewRequestActivity.this,
                                "Request Failed",
                                Toast.LENGTH_LONG);

                        failed_toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast failed_toast = Toast.makeText(NewRequestActivity.this,
                        "Request Failed",
                        Toast.LENGTH_LONG);

                failed_toast.show();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        })
        {
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

        RequestQSingleton.getInstance(NewRequestActivity.this).addToRequestQueue(strReq, REQUEST_TAG);

    }


    private Button SubmitRequestBtn;
    private ImageButton CancelRequestBtn;
    private ImageButton ClickPicBtn;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        SubmitRequestBtn = (Button) findViewById(R.id.SubmitRequestBtn);
        SubmitRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    EditText shirtText = (EditText) findViewById(R.id.teShirtCount);
                    int shirts = Integer.parseInt(shirtText.getText().toString());
                    EditText pantText = (EditText) findViewById(R.id.tePantsCount);
                    int pants = Integer.parseInt(pantText.getText().toString());
                    EditText shortsText = (EditText) findViewById(R.id.teShortsCount);
                    int shorts = Integer.parseInt(shortsText.getText().toString());
                    EditText tshirtText = (EditText) findViewById(R.id.teTShirtCount);
                    int tshirts = Integer.parseInt(tshirtText.getText().toString());
                    EditText bedsheetText = (EditText) findViewById(R.id.teBedSheetsCount);
                    int bedsheets = Integer.parseInt(bedsheetText.getText().toString());
                    EditText innerwearText = (EditText) findViewById(R.id.teUnderGarmentsCount);
                    int innerwears = Integer.parseInt(innerwearText.getText().toString());
                    EditText othersText = (EditText) findViewById(R.id.teOthersCount);
                    int others = Integer.parseInt(othersText.getText().toString());


                    final JSONObject lObj = new JSONObject();
                    try {
                        lObj.put("shirts",shirts);
                        lObj.put("pants",pants);
                        lObj.put("shorts",shorts);
                        lObj.put("tshirts",tshirts);
                        lObj.put("towels",0);
                        lObj.put("bedsheets",bedsheets);
                        lObj.put("innerwears",innerwears);
                        lObj.put("others",others);
                        lObj.put("hnum",4);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    laundryRequest(lObj);
                } catch (Exception e) {
                    e.printStackTrace();
                }




            }
        });

        CancelRequestBtn = (ImageButton) findViewById(R.id.CancelRequestBtn);
        CancelRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelRequest();
            }
        });

        ClickPicBtn = (ImageButton) findViewById(R.id.ClickPicBtn);
        ClickPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Handle Image here

    }


    public void CancelRequest(){
        Intent i;
        i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }
}
