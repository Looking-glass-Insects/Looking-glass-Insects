package com.example.heyong.eeyeswindow.Bean;

/**
 * Created by Heyong on 2017/1/21.
 * MainActivity --> FindFragment --> 热门发步方
 */

public class HotPublisherBean {
    String imgURL;
    String name;

    public HotPublisherBean() {
    }

    public HotPublisherBean(String imgURL, String name) {
        this.imgURL = imgURL;
        this.name = name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
