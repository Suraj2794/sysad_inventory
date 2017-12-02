package com.example.suraj.inventory_app.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.suraj.inventory_app.R;
import com.example.suraj.inventory_app.data.HistoryData;

import java.util.ArrayList;

/**
 * Created by Suraj Singh on 10-03-2017.
 */
public class HistoryAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<HistoryData> dataList;
    Context context;
    public HistoryAdapter(ArrayList<HistoryData> dataList, LayoutInflater inflater, Context context)
    {
        this.dataList=dataList;
        this.inflater=inflater;
        this.context=context;

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataList.size();
    }
    @Override
    public HistoryData getItem(int position) {
        // TODO Auto-generated method stub
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.history_data, null);
        }
        final TextView sys_name=(TextView)convertView.findViewById(R.id.sys_name);
        final TextView info=(TextView)convertView.findViewById(R.id.info);
        final TextView issue_date=(TextView)convertView.findViewById(R.id.issue_date);
        final TextView see_more=(TextView)convertView.findViewById(R.id.see_more);

        sys_name.setText(getItem(position).getSystem_name());
        issue_date.setText("Issue Date:- "+getItem(position).getIssue_date());
        info.setText(getItem(position).getProcessor()+" "+getItem(position).getRam_size());
        see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(getItem(position).get_themeReleased()!=0)
                {
                    Intent intent=new Intent(context, ImageShowActivity.class);
                    intent.putExtra("event_name",getItem(position).getEvent_name());
                    intent.putExtra("num",getItem(position).get_themeReleased());
                    context.startActivity(intent);
                }*/
            }
        });
        /*Typeface tf= Typeface.createFromAsset(context.getAssets(), "Comic.ttf");
        tvTitle.setTypeface(tf);
        tvtime.setTypeface(tf);
        tvwinners.setTypeface(tf);
        tvmore.setTypeface(tf);*/
        return convertView;
    }
}
