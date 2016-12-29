package com.example.heyong.eeyeswindow.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

import com.example.heyong.eeyeswindow.UI.Fragment.HomeFragment;

/**
 * 监听网络状态
 */
public class NetworkReceiver extends BroadcastReceiver {

    Handler handler;
    public NetworkReceiver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            handler.sendEmptyMessage(HomeFragment.IS_OFFLINE);
        }else {
            handler.sendEmptyMessage(HomeFragment.IS_ONLINE);
        }
    }
}
