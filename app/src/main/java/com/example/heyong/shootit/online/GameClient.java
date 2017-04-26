package com.example.heyong.shootit.online;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

/**
 *
 */
public class GameClient {
    static String TAG = "GameClient";
    private Context context;
    private SendThread clientSendThread;
    private ReceiveThread clientReceiveThread;
    private Handler handler;

    public static GameClient getInstance() {
        return Factory.client;
    }

    private GameClient() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private static class Factory {
        private static GameClient client = new GameClient();
    }

    public void connect(final int port) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo info = wifiManager.getDhcpInfo();//得到主机IP地址
        final String serverAddress = intToIp(info.serverAddress);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG,"ip-->"+serverAddress);
                    Socket client = new Socket(serverAddress, port);
                    Log.d(TAG,"connected");
                    clientSendThread = new SendThread(client);
                    clientSendThread.start();
                    clientReceiveThread = new ReceiveThread(client);
                    clientReceiveThread.setHandler(handler);
                    clientReceiveThread.start();
                    Log.d(TAG,"connected");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG,"bug");
                }
            }
        }).start();
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
    }

    /**
     *
     */
    public void disconnect() {
        if (clientReceiveThread != null)
            clientReceiveThread.close();
        if (clientSendThread != null)
            clientSendThread.close();
    }
}
