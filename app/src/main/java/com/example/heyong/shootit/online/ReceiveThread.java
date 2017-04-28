package com.example.heyong.shootit.online;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 */

public class ReceiveThread extends Thread {

    static String TAG = "ReceiveThread";


    private Socket client;
    private ObjectInputStream ois;
    private boolean isRunning = true;
    private Handler handler;
    private ConcurrentLinkedDeque workQueue = new ConcurrentLinkedDeque();

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public ReceiveThread(Socket client) {
        super();
        this.client = client;
        try {
            ois = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readObj() {
        Object o = null;
        if (workQueue.size() != 0) {
            o = workQueue.remove();
            Log.d(TAG, "readObj-->" + o.getClass().getName());
            assert o != null;
        }
        return o;
    }


    @Override
    public void run() {
        try {
            /**
             * 游戏开始标志
             */
            Object o = ois.readObject();
            if (o instanceof Integer) {
                int tag = (int) o;
                if (tag == SendThread.TAG_ON_CONNECTED) {
                    if (handler != null) {
                        handler.sendEmptyMessage(SendThread.TAG_ON_CONNECTED);
                    }
                }else if (tag == SendThread.TAG_GAME_START){
                    if (handler != null) {
                        handler.sendEmptyMessage(SendThread.TAG_GAME_START);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isRunning) {
            try {
                Object o = ois.readObject();
                if (o instanceof NetBean) {
                    NetBean bean = (NetBean) o;
                    if (!bean.isReceived) {
                        bean.setReceived(true);
                        workQueue.add(bean);
                    }
                }
                Log.d(TAG, o.getClass().getName());
                Log.d(TAG, "workQueue-->" + workQueue.size());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        try {
            isRunning = false;
            if (ois != null)
                ois.close();
            if (client != null)
                client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
