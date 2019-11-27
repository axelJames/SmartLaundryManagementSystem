package com.example.smartlms;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class HistoryActivity extends AppCompatActivity {



    void populateFromDB() {

        final String[] from = new String[] { DBHelper._ID,
                DBHelper.STATUS, DBHelper.REQUESTTIME };

        final int[] to = new int[] { R.id.Requestid, R.id.Status, R.id.time};

        DBManager dbManager;
//        SimpleCursorAdapter adapter;

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        ListView listView = (ListView) findViewById(R.id.list1);

        HistoryCursorAdaptor adapter = new HistoryCursorAdaptor(
                this, R.layout.list_item, cursor, 0 );
//        adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        // OnCLickListiner For List Items
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
//                TextView idTextView = (TextView) view.findViewById(R.id.id);
//                TextView titleTextView = (TextView) view.findViewById(R.id.title);
//                TextView descTextView = (TextView) view.findViewById(R.id.desc);
//
//                String id = idTextView.getText().toString();
//                String title = titleTextView.getText().toString();
//                String desc = descTextView.getText().toString();
//
//                Intent modify_intent = new Intent(getApplicationContext(), ModifyCountryActivity.class);
//                modify_intent.putExtra("title", title);
//                modify_intent.putExtra("desc", desc);
//                modify_intent.putExtra("id", id);
//
//                startActivity(modify_intent);
//            }
//        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        populateFromDB();
    }
}
