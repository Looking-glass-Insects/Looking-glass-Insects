package com.example.heyong.eeyeswindow.Bean;

import java.io.Serializable;

/**
 *
 */

public class HomeLectureBean implements Serializable {
    String title;
    String picURL;

    public HomeLectureBean() {
    }

    public HomeLectureBean(String title, String picURL) {
        this.title = title;
        this.picURL = picURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public String getTitle() {
        return title;
    }

    public String getPicURL() {
        return picURL;
    }
}
