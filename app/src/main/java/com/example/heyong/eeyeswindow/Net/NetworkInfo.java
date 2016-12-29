package com.example.heyong.eeyeswindow.Net;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Heyong on 2016/12/29.
 */

public class NetworkInfo {

    public static boolean isOnLine(Context context){
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        return wifi || internet;
    }
}
