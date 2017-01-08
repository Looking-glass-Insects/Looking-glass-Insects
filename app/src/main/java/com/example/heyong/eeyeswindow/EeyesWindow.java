package com.example.heyong.eeyeswindow;

import android.app.Application;
import android.content.Context;






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
        //refWatcher = LeakCanary.install(this);
    }
}
