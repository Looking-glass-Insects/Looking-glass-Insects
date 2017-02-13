package com.example.heyong.eeyeswindow;

import android.app.Application;

import com.example.heyong.eeyeswindow.Cache.DiskLruCacheHelper;


public class EeyesWindow extends Application {
   // private RefWatcher refWatcher;

//    public static RefWatcher getRefWatcher(Context context) {
//        EeyesWindow application = (EeyesWindow) context
//                .getApplicationContext();
//        return application.refWatcher;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        DiskLruCacheHelper.init(getApplicationContext());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


}
