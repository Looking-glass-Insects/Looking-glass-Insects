package com.example.heyong.eeyeswindow.UI.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.Tools.CacheUtil;
import com.example.heyong.eeyeswindow.Tools.GlideImageLoader;
import com.example.heyong.eeyeswindow.Tools.SimpleDialogFactory;
import com.example.heyong.eeyeswindow.UI.CustomView.SearchPopupWindow;
import com.example.heyong.eeyeswindow.UI.Fragment.FindFragment;
import com.example.heyong.eeyeswindow.UI.Fragment.HomeFragment;
import com.example.heyong.eeyeswindow.UI.Fragment.MoreFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.heyong.eeyeswindow.UI.CustomView.SearchPopupWindow.SUBMIT_TEXT;

/**
 * author : heYong
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @BindView(R.id.view_pager_content)
    ViewPager mainViewPager;

    List<Fragment> mainViews;
    int currFragment = 0;//中央视图index

    static final int BANNER_FINISH = -1;//轮播图自动弹起


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == BANNER_FINISH) {
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

        mainViews = new LinkedList<>();
        mainViews.add(new HomeFragment());
        mainViews.add(new FindFragment());
        mainViews.add(new MoreFragment());
        setupHeader();
        setupBottom();
        //setupDrawer();

        mainViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
        mainViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(BANNER_FINISH);
            }
        }, 5000);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 创建title布局
     */
    private void setupHeader() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_drawer_white_24dp);


        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimary));//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色

        //轮播图
        List<Integer> images = new LinkedList<>();
        images.add(R.drawable.banner1);
        images.add(R.drawable.banner2);
        Banner banner = (Banner) findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.RIGHT)
                .setImageLoader(new GlideImageLoader())
                .setImages(images).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_title, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            //Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
            SearchPopupWindow searchPopup = new SearchPopupWindow(MainActivity.this);
            int[] location = new int[2];
            View view = findViewById(R.id.toolbar);
            view.getLocationOnScreen(location);
            searchPopup.showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 10, location[1]);
        } else if (id == R.id.action_share) {
            //startActivity(new Intent(this,AboutActivity.class));
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
                        mainViewPager.setCurrentItem(0);
                        currFragment = 0;
                        break;
                    case R.id.menu_search:
                        mainViewPager.setCurrentItem(1);
                        currFragment = 1;
                        break;
                    case R.id.menu_more:
                        mainViewPager.setCurrentItem(2);
                        currFragment = 2;
                        break;
                }
                return true;
            }
        });
    }


    /**
     * 侧面抽屉布局
     */
    private Drawer drawer;
    private static String itemCacheTag = "itemCache";

    private void setupDrawer() {

        ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem().withName("点我登陆");
        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.colorAccent)))
                .addProfiles(
                        profileDrawerItem
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        Toast.makeText(MainActivity.this, "onProfileChanged", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }).withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Toast.makeText(MainActivity.this, "onProfileImageClick", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        Toast.makeText(MainActivity.this, "onProfileImageLongClick", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                })
                .build();


        PrimaryDrawerItem itemCache = new PrimaryDrawerItem()
                .withName(getResources()
                        .getString(R.string.cache_clear))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        drawer.closeDrawer();
                        SimpleDialogFactory.alertDialog(MainActivity.this, "提示", "确定清空缓存吗？", R.drawable.ic_adb_black_24dp,
                                new SimpleDialogFactory.IAlertDialogCallBack() {
                                    @Override
                                    public void doSomething(boolean isOK) {
                                        if (isOK) {
                                            CacheUtil.clearAllCache(MainActivity.this);
                                            freshCacheString();
                                            Toast.makeText(MainActivity.this, "清理完成", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        return true;
                    }
                })
                .withTag(itemCacheTag)
                .withIcon(R.drawable.ic_adb_black_24dp)
                .withBadgeStyle(new BadgeStyle()
                        .withTextColorRes(R.color.item_desc)
                        .withColorRes(R.color.colorAccent))
                .withBadge(CacheUtil.getCacheSize(this));


        drawer = new DrawerBuilder().withActivity(this)
                .withToolbar((Toolbar) findViewById(R.id.toolbar))
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        itemCache
                ).withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        freshCacheString();
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .build();

    }

    private void freshCacheString() {
        try {
            PrimaryDrawerItem itemCache = (PrimaryDrawerItem) drawer.getDrawerItem(itemCacheTag);
            itemCache.withBadge(CacheUtil.getCacheSize(MainActivity.this));
            drawer.updateItem(itemCache);
        } catch (NullPointerException e) {
            //小bug
        }
    }

    /**
     * 二维码扫描结果
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            if (result.getContents() != null) {
                //Log.d(TAG, "Scanned: " + result.getContents());
                String query = result.getContents();
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                i.putExtra(SUBMIT_TEXT, query);
                startActivity(i);
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        }
    }


    class MainViewPagerAdapter extends FragmentPagerAdapter {

        public MainViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mainViews.get(position);
        }

        @Override
        public int getCount() {
            return mainViews.size();
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            BottomNavigationView bottom = (BottomNavigationView) findViewById(R.id.bottom_navigation);
            bottom.getMenu().getItem(currFragment).setChecked(false);
            bottom.getMenu().getItem(position).setChecked(true);
            currFragment = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
