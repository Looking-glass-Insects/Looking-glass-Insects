package com.example.heyong.eeyeswindow.Presenter;

/**
 * Created by Heyong on 2017/1/26.
 */

public interface Presenter {
    void nextData(INetworkCallBack get);
    void nextData(INetworkCallBack get, int count);
}
