package com.example.suraj.inventory_app.api;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.suraj.inventory_app.data.HistoryData;
import com.example.suraj.inventory_app.listner.OnApiListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Suraj Singh on 10-03-2017.
 */
public class History_api extends AsyncTask<String,String,String> {
    Context context;
    OnApiListener listener;
    ArrayList<HistoryData> dataList;
    ProgressDialog pd;
    public final  static int CODE=1001;
    String user_id;
    public History_api(Context context, OnApiListener listener, ArrayList<HistoryData> dataList,String user_id)
    {
        this.context=context;
        this.listener=listener;
        this.dataList=dataList;
        this.user_id=user_id;
    }
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        pd=new ProgressDialog(context);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        String url="http://10.130.4.192:8000/history?user_id=admin";
        try
        {
            URL url1=new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stb.append(line + "\n");
            }
            String result=stb.toString();
            JSONObject obj=new JSONObject(result);
            publishProgress("dialog","Data Loading...");
            JSONArray array=obj.getJSONArray("result");
            for(int i=0;i<array.length();i++) {
                    JSONObject o = array.getJSONObject(i);
                    String sys_name = o.getString("name");
                    String issue_date = o.getString("issue_date");
                    String processor = o.getString("processor");
                    String ram = o.getString("ram_size");
                    String return_date = o.getString("return_date");
                    String tag = o.getString("tag");
                    String location = o.getString("location");
                    HistoryData data = new HistoryData(sys_name, tag, issue_date, return_date, processor, ram, location);
                    dataList.add(data);
                }

                return "value";

        }
        catch(Exception ex)
        {
            publishProgress("alert","There is some problem, Please try again after some time");
            return null;
        }

    }
    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        pd.dismiss();
        if(result!=null)
        {
            listener.onSuccess(CODE);
        }
        else
        {
            listener.onError(CODE);
        }
    }
    @Override
    protected void onProgressUpdate(String... values) {
        // TODO Auto-generated method stub
        super.onProgressUpdate(values);
        if(values[0].equals("dialog"))
        {
            pd.setMessage(values[1]);
        }
        else if(values[0].equals("alert"))
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setMessage(values[1]);
            builder.setTitle("Alert!");
            builder.setPositiveButton("Ok", null);
            builder.show();
        }
    }
}
