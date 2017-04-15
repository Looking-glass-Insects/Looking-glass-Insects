package com.example.heyong.myreader.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.heyong.myreader.R;

/**
 * Created by Heyong on 2017/4/14.
 *
 */
@Deprecated
public class LoadingWindow {
    private static View loadingView;

    public static void showWindow(Context context) {
        if (loadingView == null) {
            loadingView = LayoutInflater.from(context).inflate(R.layout.window_loading, null, false);
        }
        WindowManager.LayoutParams rootViewParams = new WindowManager.LayoutParams();
        rootViewParams.x = 0;
        rootViewParams.y = 0;
        rootViewParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        rootViewParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        rootViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        rootViewParams.format = PixelFormat.TRANSLUCENT;
        rootViewParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        rootViewParams.gravity = Gravity.CENTER;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(loadingView, rootViewParams);
    }

    public static void removeWindow(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeView(loadingView);
    }

}
