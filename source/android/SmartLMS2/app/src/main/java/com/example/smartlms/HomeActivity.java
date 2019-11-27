package com.example.smartlms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        Button button = (Button) findViewById(R.id.button2);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, NewRequestActivity.class);
//                HomeActivity.this.startActivity(intent);
//            }
//        });
//
//        Button hist_button = (Button) findViewById(R.id.button4);
//        hist_button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
//                HomeActivity.this.startActivity(intent);
//            }
//        });


        Button btnMenu = (Button) findViewById(R.id.MenuBtn);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMenu();
            }
        });

        Button btnMakeRequest = (Button) findViewById(R.id.MakeRequestBtn);
        btnMakeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMakeRequest();
            }
        });
    }

    public void goToMenu(){
        Intent i;
        i = new Intent(this,MenuActivity.class);
        startActivity(i);
    }

    public void goToMakeRequest(){
        Intent i;
        i = new Intent(this,NewRequestActivity.class);
        startActivity(i);
    }
}
