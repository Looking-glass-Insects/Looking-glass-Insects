package com.example.heyong.shootit.online;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 */

public class SendThread extends Thread {

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
            Iterator iterator = workQueue.iterator();
            while (iterator.hasNext()) {
                Object o = iterator.next();
                try {
                    oos.writeObject(o);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void write(Object o) {
        workQueue.add(o);
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