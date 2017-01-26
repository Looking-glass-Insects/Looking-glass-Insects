package com.example.heyong.eeyeswindow.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Heyong on 2017/1/26.
 */

public class HomeActivityBean implements Serializable,Parcelable{
    String title = "title";
    String picURL = "picURL";
    String tv1 = "tv1";//音乐
    String tv2 = "tv2";//彭康书院
    String tv3 = "tv3";//电影
    String time = "time";
    String location = "location";
    String publisher = "publisher";

    public HomeActivityBean(String title, String picURL, String tv1, String tv2, String tv3, String time, String location, String publisher) {
        this.title = title;
        this.picURL = picURL;
        this.tv1 = tv1;
        this.tv2 = tv2;
        this.tv3 = tv3;
        this.time = time;
        this.location = location;
        this.publisher = publisher;
    }

    public HomeActivityBean() {
    }

    protected HomeActivityBean(Parcel in) {
        title = in.readString();
        picURL = in.readString();
        tv1 = in.readString();
        tv2 = in.readString();
        tv3 = in.readString();
        time = in.readString();
        location = in.readString();
        publisher = in.readString();
    }

    public static final Creator<HomeActivityBean> CREATOR = new Creator<HomeActivityBean>() {
        @Override
        public HomeActivityBean createFromParcel(Parcel in) {
            return new HomeActivityBean(in);
        }

        @Override
        public HomeActivityBean[] newArray(int size) {
            return new HomeActivityBean[size];
        }
    };

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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(picURL);
        dest.writeString(tv1);
        dest.writeString(tv2);
        dest.writeString(tv3);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeString(publisher);
    }
}
