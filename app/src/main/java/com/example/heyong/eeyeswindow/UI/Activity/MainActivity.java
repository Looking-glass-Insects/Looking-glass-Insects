package com.example.heyong.eeyeswindow.UI.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.Fragment.FindFragment;
import com.example.heyong.eeyeswindow.UI.Fragment.HomeFragment;
import com.example.heyong.eeyeswindow.UI.Fragment.MoreFragment;
import com.example.heyong.eeyeswindow.UI.Tools.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  author : heYong
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.frame_content)
    FrameLayout frameContent;

    Fragment[] contents;
    int currFragment = 0;//初始时中央视图index

    static final int BANNER_FINISH = -1;//轮播图自动弹起

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == BANNER_FINISH){
                AppBarLayout bar = (AppBarLayout) findViewById(R.id.appbar);
                bar.setExpanded(false);//轮播图弹起
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupHeader();
        setupBottom();
        contents = new Fragment[3];
        contents[0] = new HomeFragment();
        contents[1] = new FindFragment();
        contents[2] = new MoreFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame_content,contents[0]);
        ft.commit();


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(BANNER_FINISH);
            }
        },5000);
    }

    /**
     * 绑定中央视图fragment
     * @param index [012]
     */
    private void bindView(int index) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (null != contents[currFragment]) {
            transaction.hide(contents[currFragment]);
        }

        currFragment = index;

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(contents[currFragment].getClass().getName());
        if (null == fragment) {
            fragment = contents[currFragment];
        }
        if (!fragment.isAdded()) {
            transaction.add(R.id.frame_content, fragment, fragment.getClass().getName());
        } else {
            transaction.show(fragment);
        }
        transaction.commit();
    }

    /**
     * 创建title布局
     */
    private void setupHeader() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
            }
        });
        mToolbar.setNavigationIcon(R.drawable.ic_drawer_black_24dp);
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle("e瞳大屏幕");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimary));//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色

        //轮播图
        List<Integer> images = new LinkedList<>();
        images.add(R.drawable.banner1);
        images.add(R.drawable.banner2);
        Banner banner = (Banner) findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setIndicatorGravity(BannerConfig.CENTER).setImageLoader(new GlideImageLoader()).setImages(images).start();
    }

    /**
     * 创建底部布局
     */
    private void setupBottom() {
        BottomNavigationView bottom = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                       bindView(0);
                        break;
                    case R.id.menu_search:
                        bindView(1);
                        break;
                    case R.id.menu_more:
                        bindView(2);
                        break;
                }
                return true;
            }
        });
    }



}
