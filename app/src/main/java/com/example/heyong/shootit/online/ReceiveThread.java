package com.example.heyong.shootit.online;

import android.os.Handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 *
 */

public class ReceiveThread extends Thread {
    Socket client;
    ObjectInputStream ois;
    boolean isRunning = true;
    Handler handler;

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


    @Override
    public void run() {
        while (isRunning) {

            try {
                double d = ois.readDouble();
                if (d == 6.25){
                    handler.sendEmptyMessage(999);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
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
