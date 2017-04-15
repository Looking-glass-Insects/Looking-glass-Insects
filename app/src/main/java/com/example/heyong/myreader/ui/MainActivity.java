package com.example.heyong.myreader.ui;

import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.heyong.myreader.MyApplication;
import com.example.heyong.myreader.R;
import com.example.heyong.myreader.ui.adapter.ListRecyclerViewAdapter;
import com.example.heyong.myreader.ui.adapter.MainRecyclerViewAdapter;
import com.example.heyong.myreader.ui.holder.ErrorHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.heyong.myreader.MyApplication.myApplication;
import static com.example.heyong.myreader.ui.adapter.MainRecyclerViewAdapter.OnGetDataListener.SUCCESS;

public class MainActivity extends AppCompatActivity {
    static String TAG = "MainActivity";

    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;
    View[] views = new View[3];
    int currView;

    //MainRecyclerViewAdapter mainRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        myApplication.registerMainActivity(this);

        myApplication.changeStatusBarColor(this);

        //mainRecyclerViewAdapter = new MainRecyclerViewAdapter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupHeader();
        setupContent();
        setupBottom();

        mainViewPager.setPageMarginDrawable(new ColorDrawable(MyApplication.myApplication.getThemeColor()));
        mainViewPager.setAdapter(new MainViewPagerAdapter());
        mainViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            BottomNavigationView bottom = (BottomNavigationView) findViewById(R.id.bottom_navigation);

            @Override
            public void onPageSelected(int position) {
                bottom.getMenu().getItem(currView).setChecked(false);
                bottom.getMenu().getItem(position).setChecked(true);
                currView = position;
            }

        });

    }

    @Override
    public void recreate() {
        //startActivity(new Intent(this,LoadingActivity.class));
        super.recreate();
        //LoadingActivity.handler.sendEmptyMessage(LoadingActivity.OK);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void setupHeader() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(myApplication.isNight())
            return;
        toolbar.setBackgroundColor(myApplication.getThemeColor());
    }

    private void setupContent() {
        for (int i = 0; i < 3; i++)
            views[i] = new View(this);
        createSecondPage();
        createThirdPage();
    }


    private void createSecondPage() {
        SecondPageManager secondPage = new SecondPageManager(LayoutInflater.from(this).inflate(R.layout.page_second, null, false));
        views[1] = secondPage.rootView;
    }

    private void createThirdPage() {
        RecyclerView recyclerViewList = creatEmptyRecyclerView();
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));
        ListRecyclerViewAdapter listAdapter = new ListRecyclerViewAdapter(this);
        recyclerViewList.setAdapter(listAdapter);
        recyclerViewList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                final int padding = getResources().getDimensionPixelOffset(R.dimen.activity_vertical_margin);
                outRect.top = outRect.bottom = padding;
                outRect.left = outRect.right = padding;
            }
        });
        views[2] = recyclerViewList;
    }

    private RecyclerView creatEmptyRecyclerView() {
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setLayoutParams(
                new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        return recyclerView;
    }


    private void setupBottom() {
        BottomNavigationView bottom = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        mainViewPager.setCurrentItem(0);
                        currView = 0;
                        break;
                    case R.id.menu_search:
                        mainViewPager.setCurrentItem(1);
                        currView = 1;
                        break;
                    case R.id.menu_more:
                        mainViewPager.setCurrentItem(2);
                        currView = 2;
                        break;
                }
                return true;
            }
        });
    }

    class MainViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views[position]);
            return views[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views[position]);
        }
    }

    /**
     * 用于管理主界面第二个界面
     */
     class SecondPageManager {
        View rootView;
        @BindView(R.id.rc_page_2)
        RecyclerView rcPage2;
        @BindView(R.id.srl_page_2)
        SwipeRefreshLayout srlPage2;

        //MainActivity context;
        MainRecyclerViewAdapter adapter;

        private RecyclerView.Adapter errorAdapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_error,null,false);
                return new ErrorHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 1;
            }
        };


        public SecondPageManager(View view) {
            this.rootView = view;
            ButterKnife.bind(this, rootView);
            rcPage2.setLayoutManager(new GridLayoutManager(MainActivity.this, MainRecyclerViewAdapter.DEFAULT_SPAN, GridLayoutManager.VERTICAL, false));
            rcPage2.setItemAnimator(new DefaultItemAnimator());
            adapter = new MainRecyclerViewAdapter(MainActivity.this);
            adapter.initData(new MainRecyclerViewAdapter.OnGetDataListener() {
                @Override
                public void onGetData(int code) {
                    onGetResponseCode(code);
                }
            });
            rcPage2.setAdapter(adapter);
            setupRefresh();
        }

        private void onGetResponseCode(int code){
            if (code == SUCCESS) {
                convertAdatper(true);
            } else {
                convertAdatper(false);
            }
        }

        private void setupRefresh() {
            srlPage2.setColorSchemeColors(myApplication.isNight()?getResources().getColor(R.color.colorPrimary):myApplication.getThemeColor());
            srlPage2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    adapter.requestData(0);
                    adapter.requestData(1);
                    adapter.requestData(2, new MainRecyclerViewAdapter.OnGetDataListener() {
                        @Override
                        public void onGetData(int code) {
                            onGetResponseCode(code);
                            srlPage2.setRefreshing(false);
                        }
                    });
                }
            });
        }

        public void convertAdatper(boolean isOnLine){
            if (isOnLine){
                rcPage2.setAdapter(adapter);
            }else {
                rcPage2.setAdapter(errorAdapter);
            }
        }
    }


}
