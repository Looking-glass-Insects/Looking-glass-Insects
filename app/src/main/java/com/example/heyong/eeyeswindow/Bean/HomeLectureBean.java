package com.example.heyong.eeyeswindow.Bean;

import java.io.Serializable;

/**
 *
 */

public class HomeLectureBean implements Serializable {
    String title;
    String picURL;
    String tv1;//音乐
    String tv2;//彭康书院
    String tv3;//电影
    String time;
    String location;
    String publisher;

    public HomeLectureBean() {
    }

    public HomeLectureBean(String title, String picURL, String tv1, String tv3, String tv2, String time, String location, String publisher) {
        this.title = title;
        this.picURL = picURL;
        this.tv1 = tv1;
        this.tv3 = tv3;
        this.tv2 = tv2;
        this.time = time;
        this.location = location;
        this.publisher = publisher;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public String getTv1() {
        return tv1;
    }

    public void setTv1(String tv1) {
        this.tv1 = tv1;
    }

    public String getTv2() {
        return tv2;
    }

    public void setTv2(String tv2) {
        this.tv2 = tv2;
    }

    public String getTv3() {
        return tv3;
    }

    public void setTv3(String tv3) {
        this.tv3 = tv3;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
