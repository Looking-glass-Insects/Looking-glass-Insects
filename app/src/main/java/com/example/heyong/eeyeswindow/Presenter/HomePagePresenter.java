package com.example.heyong.eeyeswindow.Presenter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;

import com.example.heyong.eeyeswindow.Bean.Bean;
import com.example.heyong.eeyeswindow.Bean.HomeLectureBean;
import com.example.heyong.eeyeswindow.Cache.CacheManager;

import com.example.heyong.eeyeswindow.Net.HomePageData;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;

/**
 * 数据提供
 */
@Deprecated
public class HomePagePresenter {
    static String TAG = "HomePagePresenter";
    private Context context;
    private HomePageDataListener listener;//数据传输终点
    private CacheManager manager;//缓存
    public static String CACHE_OBJ = "object";

    public HomePagePresenter(Context context, HomePageDataListener listener) {
        this.context = context;
        this.listener = listener;
        manager = new CacheManager(context);
    }

    public void cachedData() {
        Object obj = manager.getCache(CACHE_OBJ, CACHE_OBJ);
        if (obj == null) {

        } else {
            List<HomeLectureBean> beanList = (LinkedList<HomeLectureBean>) obj;
            listener.onGetData(beanList);
        }
        return;
    }

    /**
     * 获取下一条数据
     * @param onGetDataSuccess
     */
    public void nextData(final OnGetDataSuccessByNet onGetDataSuccess) {
        HomePageData.dataCallBack(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                final List<HomeLectureBean> beanList = new LinkedList<>();
                try {
                    beanList.add(new HomeLectureBean());
                } catch (Exception e) {
                    onGetDataSuccess.onGetData(false);
                }
                listener.onGetData(beanList);
                onGetDataSuccess.onGetData(true);
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                onGetDataSuccess.onGetData(false);
            }
        });
    }

    public void startCache(final String uniqueName, final String URL_OR_STR, final Serializable content, @Nullable Subscriber<? super String> subscriber) {
        manager.startCache(uniqueName, URL_OR_STR, content, subscriber);
    }

    public interface HomePageDataListener {
        void onGetData(List<HomeLectureBean> beanList);
    }

    public interface OnGetDataSuccessByNet {
        void onGetData(boolean isSuccessful);
    }
}
