package com.example.heyong.shootit.online;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 */
public class GameServer {

    static String TAG = "GameServer";
    private Handler handler;

    private GameServer() {
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public static GameServer getInstance() {
        return Factory.server;
    }

    private static class Factory {
        private static GameServer server = new GameServer();
    }

    private SendThread serverSendThread;
    private ReceiveThread serverReceiveThread;


    public void writeObj(Object o){
        serverSendThread.write(o);
    }
    /**
     * 开启服务器
     */
    public void startServer(final int port) {
        Log.d(TAG, "-->startServer");
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 开启服务端
                try {
                    ServerSocket server = new ServerSocket(port);
                    Socket client = server.accept();//阻塞
                    //连接后启动数据交互线程
                    serverSendThread = new SendThread(client);
                    serverSendThread.start();
                    serverReceiveThread = new ReceiveThread(client);
                    serverReceiveThread.start();
                    serverReceiveThread.setHandler(handler);
                    Log.d(TAG, "-->start");
                } catch (IOException e) {
                    Log.d(TAG, "-->bug");
                }
            }
        }).start();
    }

    /**
     * 关闭服务器
     */
    public void close() {
        if (serverReceiveThread != null)
            serverReceiveThread.close();
        if (serverSendThread != null)
            serverSendThread.close();
        Log.d(TAG, "-->close");
    }

}
