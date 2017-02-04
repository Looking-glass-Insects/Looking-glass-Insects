package com.example.heyong.eeyeswindow.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.example.heyong.eeyeswindow.Bean.HomeActivityBean;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.Adapter.ActivityDetailViewHolder;
import com.example.heyong.eeyeswindow.UI.Adapter.HomePageLectureAdapter;
import com.example.heyong.lib.swipeBackActivity.SwipeBackActivity;

import java.util.Arrays;

public class ActivityDetailActivity extends SwipeBackActivity {
    ActivityDetailViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_detail, null);
        setContentView(view);
        viewHolder = new ActivityDetailViewHolder(view, this);
        setupHeader();
        setupContent();
    }

    private void setupContent() {
        viewHolder.yeildText(Arrays.asList(new String[]{"标签1", "标签2", "标签3"}));
        viewHolder.setContentTitle("活动详情:");
        viewHolder.setBannerImage(R.drawable.banner2);
        viewHolder.setContent("什么也没有,喵喵喵");
    }

    private void setupHeader() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        HomeActivityBean bean = (HomeActivityBean) intent.getSerializableExtra(HomePageLectureAdapter.BEAN);
        toolbar.setTitle("活动详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
