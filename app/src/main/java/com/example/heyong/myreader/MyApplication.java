package com.example.heyong.myreader;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

import com.example.heyong.myreader.tool.StatusBarUtils;
import com.example.heyong.myreader.tool.ThemeManager;

/**
 * Created by Heyong on 2017/4/14.
 */

public class MyApplication extends Application {
    static String TAG = "MyApplication";
    static private ThemeManager manager;
    public static MyApplication myApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        manager = new ThemeManager(this);
        manager.loadThemeState();
        myApplication = this;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        manager = null;
    }

    /**
     * @param
     */
    public void registerMainActivity(AppCompatActivity activity) {
        manager.registerActivity(activity);
    }

    /**
     * @param activity
     */
    public void changeStatusBarColor(Activity activity) {
        if (manager.isNight())
            return;
        StatusBarUtils.setWindowStatusBarColor(activity, manager.getThemeColor());
    }

    public boolean isNight() {
        return manager.isNight();
    }

    public void switchNightTheme() {
        manager.switchNightTheme();
    }

    public void changeThemeColor(int color) {
        manager.changeColor(color);
        manager.recreate();
    }


    public int getThemeColor() {
        return manager.getThemeColor();
    }

    public Bitmap buildBitmap(int res) {
        return manager.buildBitmap(res);
    }

}
