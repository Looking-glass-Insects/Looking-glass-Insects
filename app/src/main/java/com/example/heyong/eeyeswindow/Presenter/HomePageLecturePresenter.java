package com.example.heyong.eeyeswindow.Presenter;


import android.content.Context;

import com.example.heyong.eeyeswindow.Bean.HomeLectureBean;
import com.example.heyong.eeyeswindow.Cache.DiskLruCacheHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * 数据提供
 */

public class HomePageLecturePresenter implements Presenter {
    static String TAG = "HomePagePresenter";
    static String DIR = "object";//文件夹
    static String KEY = "LinkedList<HomeLectureBean>";//键

    private Context context;
    private HomePageLectureDataListener listener;//数据传输终点


    public static String HOME_PAGE_LIST = "HOME_PAGE_LIST";

    int count = 0;

    public HomePageLecturePresenter(Context context, HomePageLectureDataListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void cachedData() {
        DiskLruCacheHelper.getCache(new DiskLruCacheHelper.ReadCallBack() {
            @Override
            public String dir() {
                return DIR;
            }

            @Override
            public String key() {
                return KEY;
            }

            @Override
            public void onGetInputStream(InputStream is) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(is);
                    Object obj = ois.readObject();
                    if (obj instanceof LinkedList) {
                        List<HomeLectureBean> beanList = (LinkedList<HomeLectureBean>) obj;
                        listener.onGetData(beanList);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取下一条数据
     */
    @Override
    public void nextData(INetworkCallBack get) {
        if(count == 15){
            get.onGetData(INetworkCallBack.DATA_FINISH);
            return;
        }
        final List<HomeLectureBean> beanList = new LinkedList<>();
        beanList.add(new HomeLectureBean());
        listener.onGetData(beanList);
        count++;
        get.onGetData(INetworkCallBack.SUCCESS);
    }

    @Override
    public void nextData(INetworkCallBack get, int count) {
        final List<HomeLectureBean> beanList = new LinkedList<>();
        for (int i = 0; i < count; i++)
            beanList.add(new HomeLectureBean());
        listener.onGetData(beanList);
       // cachedData();
        get.onGetData(INetworkCallBack.SUCCESS);
    }


    public void startCache(final Serializable content) {
        DiskLruCacheHelper.writeCache(new DiskLruCacheHelper.WriteCallBack() {
            @Override
            public String dir() {
                return DIR;
            }

            @Override
            public String key() {
                return KEY;
            }

            @Override
            public long maxSize() {
                return 0;
            }

            @Override
            public boolean onGetStream(OutputStream os) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    oos.writeObject(content);
                    oos.flush();
                    oos.close();
                } catch (IOException e) {
                    return false;
                }
                return true;
            }
        });
    }


    public interface HomePageLectureDataListener {
        void onGetData(List<HomeLectureBean> beanList);
    }

}
