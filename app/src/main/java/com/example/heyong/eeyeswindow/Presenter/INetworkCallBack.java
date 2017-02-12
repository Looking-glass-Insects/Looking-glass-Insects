package com.example.heyong.eeyeswindow.Presenter;

/**
 * Created by Heyong on 2017/1/26.
 */

public interface INetworkCallBack {
    int FALSE_TO_GET = 0;//从网络获取数据失败
    int DATA_FINISH = 1;//数据加载完毕
    int SUCCESS = 2;//成功获取数据

    void onGetData(int code);
}