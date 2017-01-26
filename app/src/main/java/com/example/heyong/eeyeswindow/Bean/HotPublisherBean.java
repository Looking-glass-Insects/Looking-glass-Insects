package com.example.heyong.eeyeswindow.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Heyong on 2017/1/21.
 * MainActivity --> FindFragment --> 热门发步方
 */

public class HotPublisherBean implements Parcelable, Serializable {
    String imgURL = "imgURL";
    String name = "name";

    public HotPublisherBean() {
    }

    public HotPublisherBean(String imgURL, String name) {
        this.imgURL = imgURL;
        this.name = name;
    }

    protected HotPublisherBean(Parcel in) {
        imgURL = in.readString();
        name = in.readString();
    }

    public static final Creator<HotPublisherBean> CREATOR = new Creator<HotPublisherBean>() {
        @Override
        public HotPublisherBean createFromParcel(Parcel in) {
            return new HotPublisherBean(in);
        }

        @Override
        public HotPublisherBean[] newArray(int size) {
            return new HotPublisherBean[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgURL);
        dest.writeString(name);
    }
}
