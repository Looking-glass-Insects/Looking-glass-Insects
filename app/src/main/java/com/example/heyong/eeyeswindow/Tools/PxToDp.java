package com.example.heyong.eeyeswindow.Tools;

import android.content.Context;

/**
 * Created by Heyong on 2017/1/6.
 */

public class PxToDp {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
