package com.example.heyong.shootit.online;

import java.io.Serializable;

/**
 *
 */

public class NetBean implements Serializable {
    boolean isRecieved = false;
    String data = "";

    public NetBean() {
    }

    public NetBean(boolean isRecieved, String data) {
        this.isRecieved = isRecieved;
        this.data = data;
    }

    public boolean isRecieved() {
        return isRecieved;
    }

    public void setRecieved(boolean recieved) {
        isRecieved = recieved;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
