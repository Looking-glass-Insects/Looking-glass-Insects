package com.example.heyong.shootit.online;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 */

public class SendThread extends Thread {
    static String TAG = "SendThread";
    /**
     * client 以连接
     */
    public static final int TAG_ON_CONNECTED = 1234;
    /**
     * 向client 发送游戏开始
     */
    public static final int TAG_GAME_START = 12414;

    private Socket socket;
    private ObjectOutputStream oos = null;
    private boolean isRunning = true;

    private ConcurrentLinkedDeque workQueue = new ConcurrentLinkedDeque();

    public SendThread(Socket client) {
        super();
        this.socket = client;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
//            Iterator iterator = workQueue.iterator();
//            synchronized (iterator){
//                while (iterator.hasNext()) {
//                    Object o = iterator.next();
//                    try {
//                        oos.writeObject(o);
//                        Log.d(TAG, "workQueue-->" + workQueue.size());
//                        iterator.remove();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
            if (workQueue.size() != 0){
                Object o = workQueue.remove();
                try {
                    oos.writeObject(o);
                    oos.flush();
                    Log.d(TAG, "workQueue-->" + workQueue.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }


    public void write(Object o) {
        synchronized (workQueue){
            workQueue.add(o);
        }
        Log.d(TAG, "write-->" + o.getClass().getName());
    }

    public void close() {
        try {
            isRunning = false;
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}