package com.example.heyong.eeyeswindow.Presenter;

import android.content.Context;

import com.example.heyong.eeyeswindow.Bean.HomeActivityBean;
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
 * Created by Heyong on 2017/1/26.
 */

public class HomePageActivityPresenter implements Presenter{
    HomePageActivityDataListener listener;
    Context context;

    static String DIR = "object";//文件夹
    static String KEY = "LinkedList<HomeActivityBean>";//键


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

    public void startCache(final Serializable content){
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

    public void cacheData(){
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
                        LinkedList<HomeActivityBean> beanList = (LinkedList<HomeActivityBean>) obj;
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

   public interface HomePageActivityDataListener{
        void onGetData(List<HomeActivityBean> list);
    }
}
