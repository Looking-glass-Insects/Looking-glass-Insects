package com.example.heyong.eeyeswindow.Presenter;


import android.content.Context;

import com.example.heyong.eeyeswindow.Bean.HomeLectureBean;
import com.example.heyong.eeyeswindow.Cache.CacheManager;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * 数据提供
 */

public class HomePagePresenter {
    static String TAG = "HomePagePresenter";
    private Context context;
    private HomePageDataListener listener;//数据传输终点
    private CacheManager manager;//缓存

    public static String HOME_PAGE_LIST = "HOME_PAGE_LIST";

    public HomePagePresenter(Context context, HomePageDataListener listener) {
        this.context = context;
        this.listener = listener;
        manager = new CacheManager(context);
    }

    public void cachedData() {
        Object obj = manager.getCache(CacheManager.CACHE_OBJ, HOME_PAGE_LIST);
        if (obj != null) {
            List<HomeLectureBean> beanList = (LinkedList<HomeLectureBean>) obj;
            listener.onGetData(beanList);
        }
    }

    /**
     * 获取下一条数据
     *
     * @param onGetDataSuccess
     */
    public void nextData(final OnGetDataSuccessByNet onGetDataSuccess,final int count) {
//        HomePageData.dataCallBack(new Callback<Bean>() {
//            @Override
//            public void onResponse(Call<Bean> call, Response<Bean> response) {
//                final List<HomeLectureBean> beanList = new LinkedList<>();
//                try {
//                    beanList.add(new HomeLectureBean());
//                } catch (Exception e) {
//                    onGetDataSuccess.onGetData(false);
//                }
//                listener.onGetData(beanList);
//                onGetDataSuccess.onGetData(true);
//            }
//
//            @Override
//            public void onFailure(Call<Bean> call, Throwable t) {
//                onGetDataSuccess.onGetData(false);
//            }
//        });
        final List<HomeLectureBean> beanList = new LinkedList<>();
        for (int i = 0; i < count; i++)
            beanList.add(new HomeLectureBean());
        listener.onGetData(beanList);
        onGetDataSuccess.onGetData(true);

    }

    public void startCache(final Serializable content) {
        manager.startCache(CacheManager.CACHE_OBJ, HOME_PAGE_LIST, content);
    }

    public interface HomePageDataListener {
        void onGetData(List<HomeLectureBean> beanList);
    }

    public interface OnGetDataSuccessByNet {
        void onGetData(boolean isSuccessful);
    }
}
