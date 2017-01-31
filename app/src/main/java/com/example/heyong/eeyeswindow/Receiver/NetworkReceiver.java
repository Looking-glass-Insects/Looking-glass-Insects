package com.example.heyong.eeyeswindow.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

/**
 * 监听网络状态
 */
public class NetworkReceiver extends BroadcastReceiver {

    Handler handler;
    public static final int IS_OFFLINE = 0;
    public static final int IS_ONLINE = 1;

    public NetworkReceiver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            handler.sendEmptyMessage(IS_OFFLINE);
        }else {
            handler.sendEmptyMessage(IS_ONLINE);
        }
    }
}
