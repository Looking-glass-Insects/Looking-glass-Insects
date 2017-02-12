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

public class HomePageLecturePresenter implements Presenter {
    static String TAG = "HomePagePresenter";
    private Context context;
    private HomePageLectureDataListener listener;//数据传输终点
    private CacheManager manager;//缓存

    public static String HOME_PAGE_LIST = "HOME_PAGE_LIST";

    public HomePageLecturePresenter(Context context, HomePageLectureDataListener listener) {
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
     */
    @Override
    public void nextData(OnGetDataSuccessByNet get) {
        final List<HomeLectureBean> beanList = new LinkedList<>();
        beanList.add(new HomeLectureBean());
        listener.onGetData(beanList);
        get.onGetData(true);
    }

    @Override
    public void nextData(OnGetDataSuccessByNet get,int count) {
        final List<HomeLectureBean> beanList = new LinkedList<>();
        for (int i = 0; i < count; i++)
            beanList.add(new HomeLectureBean());
        listener.onGetData(beanList);
        get.onGetData(true);
    }


    public void startCache(final Serializable content) {
        manager.startCache(CacheManager.CACHE_OBJ, HOME_PAGE_LIST, content);
    }


    public interface HomePageLectureDataListener {
        void onGetData(List<HomeLectureBean> beanList);
    }

}
