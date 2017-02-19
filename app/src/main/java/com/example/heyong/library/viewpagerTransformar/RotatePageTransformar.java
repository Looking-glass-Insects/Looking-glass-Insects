package com.example.heyong.library.viewpagerTransformar;

import android.support.v4.view.ViewPager;
import android.view.View;


class RotatePageTransformar implements ViewPager.PageTransformer {
    int degree = 30;

    @Override
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();
        if (position < -1) {
            //  age.setRotation(0);
        } else if (position <= 1) {
            page.setPivotX(pageWidth / 2);
            page.setPivotY(page.getHeight());
            page.setRotation(degree * position);
        } /*else if (position <= 1) {

            }*/ else {

        }

    }
}
