package com.example.smartlms;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class PendingRequestAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView ReqIDTxt;
        TextView UNameTxt;
    }

    public PendingRequestAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.req_list_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Log.d("bbbbbbbbbbbbbbbbb",Integer.toString(position));
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;

        Intent i = new Intent(mContext, FetchDetailsActivity.class);
        Integer re_id = dataModel.getId();
        i.putExtra("REQ_ID", re_id);
        mContext.startActivity(i);
//        switch (v.getId())
//        {
//            case R.id.item_info:
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
//        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.req_list_item, parent, false);
            viewHolder.ReqIDTxt = (TextView) convertView.findViewById(R.id.secondLine);
            viewHolder.UNameTxt = (TextView) convertView.findViewById(R.id.firstLine);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.UNameTxt.setText(dataModel.getName());
        viewHolder.ReqIDTxt.setText(Integer.toString(dataModel.getId()));


        // Return the completed view to render on screen
        return convertView;
    }
}
