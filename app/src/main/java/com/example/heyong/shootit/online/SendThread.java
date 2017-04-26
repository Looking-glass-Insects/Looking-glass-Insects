package com.example.heyong.shootit.online;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 */

public  class SendThread extends Thread {
    Socket socket;
    ObjectOutputStream oos = null;
    boolean isRunning = true;



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
        while (true) {

            try {
                oos.writeDouble(6.25);
                oos.flush();
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
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}