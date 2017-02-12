package com.example.heyong.eeyeswindow.Presenter;

import android.content.Context;

import com.example.heyong.eeyeswindow.Bean.HomeActivityBean;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Heyong on 2017/1/26.
 */

public class HomePageActivityPresenter implements Presenter{
    HomePageActivityDataListener listener;
    Context context;
    public HomePageActivityPresenter(Context context,HomePageActivityDataListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void nextData(INetworkCallBack get) {
        final List<HomeActivityBean> beanList = new LinkedList<>();
        beanList.add(new HomeActivityBean());
        listener.onGetData(beanList);
        get.onGetData(INetworkCallBack.SUCCESS);
    }

    @Override
    public void nextData(INetworkCallBack get, int count) {
        final List<HomeActivityBean> beanList = new LinkedList<>();
        for (int i = 0; i < count; i++)
            beanList.add(new HomeActivityBean());
        listener.onGetData(beanList);
        get.onGetData(INetworkCallBack.SUCCESS);
    }


   public interface HomePageActivityDataListener{
        void onGetData(List<HomeActivityBean> list);
    }
}
