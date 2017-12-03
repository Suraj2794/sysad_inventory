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
     * This Variable suggest the current logged in User.
     */
    static public String name = null;

    /**
     * This function is use to get the User Id of logged User.
     * This function is called several time in application, whenever user id is needed.
     * @param activity The Context in which this method is called
     * @return The User Id
     */
    static public String getUserID(Activity activity){
        if(name == null) {
            SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getString(R.string.userDetailSharedPref),
                    Context.MODE_PRIVATE);
            name = sharedPreferences.getString(activity.getString(R.string.LoggedUserId), null);
        }
        return name;
    }

    /**
     * This method is used to update the User ID shared preferences.
     * This is called while User Logs In.
     * @param activity The Context/Activity in which this method is called
     */
    static public void UpdateId(Activity activity,String userId){
        SharedPreferences userDetail =
                activity.getSharedPreferences(activity.getString(R.string.userDetailSharedPref),
                        Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = userDetail.edit();
        edit.putString(activity.getString(R.string.LoggedUserId),userId);
        edit.apply();

        name = userId;
    }

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
