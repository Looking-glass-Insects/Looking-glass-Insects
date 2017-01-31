package com.example.heyong.eeyeswindow.Tools;

import android.content.Context;

/**
 * Created by Heyong
 */

public class CacheUtil {

    public static String getCacheSize(Context context) throws Exception {
        return GlideCacheUtil.getInstance().getCacheSize(context);
    }


    public static void clearAllCache(Context context){
        GlideCacheUtil.getInstance().clearImageAllCache(context);
    }

}
