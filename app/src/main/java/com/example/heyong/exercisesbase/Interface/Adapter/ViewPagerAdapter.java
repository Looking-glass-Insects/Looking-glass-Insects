package com.example.heyong.exercisesbase.Interface.Adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> content;
    private Context context;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> content, Context context) {
        super(fm);
        this.content = content;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return content.get(position);
    }

    @Override
    public int getCount() {
        return content.size();
    }



}
