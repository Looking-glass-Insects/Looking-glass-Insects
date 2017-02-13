package com.example.heyong.eeyeswindow.UI.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.heyong.eeyeswindow.Bean.HomeActivityBean;
import com.example.heyong.eeyeswindow.Bean.HomeLectureBean;
import com.example.heyong.eeyeswindow.Bean.HotPublisherBean;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.Tools.CircleRadiusTransformation;
import com.example.heyong.eeyeswindow.Tools.PxToDp;
import com.example.heyong.eeyeswindow.UI.Adapter.ActivityBeanRecyclerViewAdapter;
import com.example.heyong.eeyeswindow.UI.Adapter.LectureBeanRecyclerViewAdapter;
import com.example.heyong.eeyeswindow.UI.Adapter.SearchFragmentHotAdapter;
import com.example.heyong.eeyeswindow.UI.CustomView.EmptyRecyclerView;
import com.example.heyong.lib.swipeBackActivity.SwipeBackActivity;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;

public class HotDetailActivity extends SwipeBackActivity {

    private static final String TAG = "HotDetailActivity";

    String[] titles = {"全部讲座", "全部活动"};
    View[] views = {
            null, null
    };//视图
    private LectureBeanRecyclerViewAdapter adapter;
    private ActivityBeanRecyclerViewAdapter activityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_detail);
        ButterKnife.bind(this);
        setupHeader();
        setupContent();
    }

    private void setupContent() {
        views[0] = LayoutInflater.from(this).inflate(R.layout.activity_hot_detail_view,null);
        initView0();
        views[1] = LayoutInflater.from(this).inflate(R.layout.activity_hot_detail_view,null);
        initView1();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyPageDapater());
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private void initView1() {

        EmptyRecyclerView recyclerView = (EmptyRecyclerView) views[1].findViewById(R.id.rc_hot_detail_lecture);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        NestedScrollView layout = new NestedScrollView(this);
        layout.setLayoutParams(new NestedScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(LayoutInflater.from(this).inflate(R.layout.empty_view,null,false));
        recyclerView.setEmptyView(layout);
        activityAdapter = new ActivityBeanRecyclerViewAdapter(this);
        List<HomeActivityBean> l = new LinkedList<>();
        for(int i = 0;i<5;i++)
            l.add(new HomeActivityBean());
        //activityAdapter.addData(l);
        recyclerView.setAdapter(activityAdapter);
    }

    private void initView0() {
        EmptyRecyclerView recyclerView = (EmptyRecyclerView) views[0].findViewById(R.id.rc_hot_detail_lecture);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        NestedScrollView layout = new NestedScrollView(this);
        layout.setLayoutParams(new NestedScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(LayoutInflater.from(this).inflate(R.layout.empty_view,null,false));
        recyclerView.setEmptyView(layout);
        adapter = new LectureBeanRecyclerViewAdapter(this);
        List<HomeLectureBean> l = new LinkedList<>();
        for(int i = 0;i<10;i++)
            l.add(new HomeLectureBean());
        adapter.addData(l);
        recyclerView.setAdapter(adapter);
    }

    private void setupHeader() {
        Intent intent = getIntent();
        HotPublisherBean bean = (HotPublisherBean)intent.getSerializableExtra(SearchFragmentHotAdapter.HOT_BEAN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Glide.with(this).load(R.drawable.i2)
                .centerCrop()
                .bitmapTransform(new CircleRadiusTransformation(this, PxToDp.dip2px(this,10),getResources().getColor(R.color.cardview_light_background)))
                .into((ImageView) findViewById(R.id.iv_header_profile));
        Glide.with(this).load(R.drawable.banner1)
                .centerCrop()
                .into((ImageView) findViewById(R.id.iv_header_bg));
        CollapsingToolbarLayout layout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        layout.setTitle(bean.getName());
        layout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimary));
        layout.setCollapsedTitleTextColor(Color.WHITE);
    }


    class MyPageDapater extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views[position]);//添加页卡
            return views[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];//页卡标题
        }
    }
}
