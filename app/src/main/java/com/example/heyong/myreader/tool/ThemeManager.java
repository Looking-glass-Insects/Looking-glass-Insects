package com.example.heyong.myreader.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.example.heyong.library.tools.BitmapHelper;
import com.example.heyong.myreader.R;

/**
 * Created by Heyong on 2017/4/14.
 */

public class ThemeManager {
    public static final String IS_NIGHT = "isNight";

    private Context context;
    private boolean isNight = false;
    private AppCompatActivity mainActivity;
    private int themeColor;
    private SparseArrayCompat<Bitmap> cachedBitmap = new SparseArrayCompat<>();

    public ThemeManager(Context context) {
        this.context = context;
    }

    public void saveThemeState() {
        SharedPreferences sp = context.getSharedPreferences(IS_NIGHT, 0);
        sp.edit().putBoolean("night", isNight)
                .putInt("themeColor", themeColor).apply();
    }

    public void loadThemeState() {
        SharedPreferences sp = context.getSharedPreferences(IS_NIGHT, 0);
        isNight = sp.getBoolean("night", false);
        int colorPrimary = context.getResources().getColor(R.color.colorPrimary);
        themeColor = sp.getInt("themeColor", colorPrimary);
    }

    public void switchNightTheme() {
        isNight = !isNight;
        applyTheme();
        saveThemeState();
        recreate();
    }


    public void recreate() {
        cachedBitmap = new SparseArrayCompat<>();
        mainActivity.recreate();
    }


    public void changeColor(int newColor) {
        this.themeColor = newColor;
        saveThemeState();
    }


    private void applyTheme() {
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public int getThemeColor() {
//        if (isNight()){
//            return context.getResources().getColor(R.color.colorPrimary);
//        }
        return themeColor;
    }


    /**
     * 传入 {@link com.example.heyong.myreader.ui.MainActivity}
     *
     * @param activity
     */
    public void registerActivity(@NonNull AppCompatActivity activity) {
        mainActivity = activity;
        applyTheme();
    }


    public boolean isNight() {
        return isNight;
    }

    public Bitmap buildBitmap(int res) {
        Bitmap cached = cachedBitmap.get(res);
        if (cached != null)
            return cached;
        Drawable drawable = context.getDrawable(res);
        Bitmap bitmap = BitmapHelper.convertDrawableToBitmap(drawable);
        Bitmap b = null;
        if (isNight) {
            b = BitmapHelper.changeImageColor(bitmap, 0xa4a2a2);
        }else {
            b = BitmapHelper.changeImageColor(bitmap, themeColor);
        }
        cachedBitmap.put(res, b);
        return b;
    }
}
