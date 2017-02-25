package com.example.heyong.library.viewpagerTransformar;

import android.support.v4.view.ViewPager;
import android.view.View;


class RotatePageTransformar implements ViewPager.PageTransformer {

    private int degree = 30;

    @Override
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();
        if (position <= 1) {
            page.setPivotX(pageWidth / 2);
            page.setPivotY(page.getHeight());
            page.setRotation(degree * position);
        }
    }

    public RotatePageTransformar(int degree) {
        this.degree = degree;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
}
