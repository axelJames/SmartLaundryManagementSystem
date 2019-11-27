package com.example.smartlms;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class HistoryCursorAdaptor extends ResourceCursorAdapter {

    public HistoryCursorAdaptor(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final String[] from = new String[] { DBHelper._ID,
                DBHelper.STATUS, DBHelper.REQUESTTIME };
        TextView id = (TextView) view.findViewById(R.id.Requestid);
        id.setText(cursor.getString(cursor.getColumnIndex(DBHelper._ID)));

        TextView status = (TextView) view.findViewById(R.id.Status);
        if(cursor.getInt(cursor.getColumnIndex(DBHelper.STATUS))==0)
            status.setText("Queued");
        else if(cursor.getInt(cursor.getColumnIndex(DBHelper.STATUS))==1)
            status.setText("Accepted");
        else if(cursor.getInt(cursor.getColumnIndex(DBHelper.STATUS))==2)
            status.setText("Rejected");
        else
            status.setText("Finished");

        TextView reqtime = (TextView) view.findViewById(R.id.time);
        reqtime.setText("Reqest id"+cursor.getString(cursor.getColumnIndex(DBHelper.REQUESTTIME)));

        TextView pos = (TextView) view.findViewById(R.id.Position);
        pos.setText(cursor.getString(cursor.getColumnIndex(DBHelper.POSITION)));
    }
}