package com.example.heyong.eeyeswindow.Tools;

import android.content.Context;

import com.example.heyong.eeyeswindow.Cache.CacheManager;

/**
 * Created by Heyong
 */

public class CacheUtil {

    public static String getCacheSize(Context context){
        CacheManager manager = new CacheManager(context);
        long size = manager.getAllLongSize() + GlideCacheUtil.getInstance().getCacheLongSize(context);
        return GlideCacheUtil.getFormatSize(size);
    }


    public static void clearAllCache(Context context){
        CacheManager manager = new CacheManager(context);
        manager.clearAllCache();
        GlideCacheUtil.getInstance().clearImageAllCache(context);
    }

}
