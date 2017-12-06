package com.example.suraj.inventory_app.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.suraj.inventory_app.R;

/**
 * This Class holds the functions having static work.
 * All this functions are developed,so that same work will not be repeated
 * in many file.
 */
public class UtilClass {

    /**
     * To Check if User is connected to internet or not.
     * @param context The Context in which this method is called
     * @return True if user is connected otherwise false.
     */

    static public boolean isConnected(Context context){

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}