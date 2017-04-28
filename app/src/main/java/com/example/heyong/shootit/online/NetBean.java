package com.example.heyong.shootit.online;

import java.io.Serializable;

/**
 * 网络数据交换
 */

public class NetBean implements Serializable {

    boolean isReceived = false;
    String bulletClass;
    int count;



    public NetBean() {
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setReceived(boolean received) {
        isReceived = received;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBulletClass() {
        return bulletClass;
    }

    public void setBulletClass(String bulletClass) {
        this.bulletClass = bulletClass;
    }
}
