package com.example.heyong.eeyeswindow.UI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.heyong.eeyeswindow.Presenter.HomePagePresenter;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.Adapter.HomePageLectureListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by HeYong
 */

public class HomeFragment extends Fragment {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.vp_view)
    ViewPager viewPager;

    String[] titles = {"讲座", "活动"};
    View[] views = {
            null, null
    };//view pager 视图


    /**
     * view[0]
     */
    ListView lvHomeLecture;
    SwipeRefreshLayout srlHome;
    View footer;


    HomePagePresenter presenter;//数据提供

    /**
     * 网络状态
     */
    @BindView(R.id.ll_off_line)
    LinearLayout llOffLine;
    NetWorkManager manager;

    static final int IS_ONLINE = 0;
    static final int IS_OFFLINE = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == IS_ONLINE){
                llOffLine.setVisibility(View.GONE);
                footer.setVisibility(View.VISIBLE);
            }else if(msg.what == IS_OFFLINE){
                footer.setVisibility(View.GONE);
                llOffLine.setVisibility(View.VISIBLE);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        views[0] = LayoutInflater.from(this.getActivity()).inflate(R.layout.home_pager_view_1, null);
        views[1] = LayoutInflater.from(this.getActivity()).inflate(R.layout.home_pager_view_2, null);
        //view[0] init
        lvHomeLecture = (ListView) views[0].findViewById(R.id.lv_home_lecture);
        View footerParent = LayoutInflater.from(this.getActivity()).inflate(R.layout.load_more, null);
        lvHomeLecture.addFooterView(footerParent);
        footer = footerParent.findViewById(R.id.footer);
        lvHomeLecture.setOnScrollListener(new MyOnScrollListener());
        srlHome = (SwipeRefreshLayout) views[0].findViewById(R.id.srl_home_1);
        srlHome.setColorSchemeResources(R.color.colorPrimary);
        srlHome.setOnRefreshListener(new MyOnRefreshListener());
        //view[1] init

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        manager.stop();
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        tabs.addTab(tabs.newTab().setText(titles[0]));
        tabs.addTab(tabs.newTab().setText(titles[1]));
        MyViewPageAdapter adapter = new MyViewPageAdapter(this.getActivity());
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        bindData();
        manager = new NetWorkManager();
        new Thread(manager).start();
        return view;
    }

    /**
     * 初始化时调用
     * <p>
     * presenter 和 listView 的 adapter 将会刷新
     */
    private void bindData() {
        srlHome.setRefreshing(true);
        HomePageLectureListAdapter adapter = new HomePageLectureListAdapter(getContext());
        presenter = new HomePagePresenter(getContext(), adapter);
        if (presenter.isOnLine()) {
            for (int i = 0; i < 10; i++) {
                presenter.nextData(new HomePagePresenter.OnGetDataSuccessByNet() {
                    @Override
                    public void onGetData(boolean isSuccessful) {
                        if (isSuccessful) {
                            srlHome.setRefreshing(false);
                            cache();
                        } else {
                            srlHome.setRefreshing(false);
                        }
                    }
                });
            }
        } else {
            presenter.cachedData();
            srlHome.setRefreshing(false);
        }
        lvHomeLecture.setAdapter(adapter);
    }

    /**
     * 缓存
     */
    private void cache() {
        HomePageLectureListAdapter adapter = (HomePageLectureListAdapter) ((HeaderViewListAdapter) lvHomeLecture.getAdapter()).getWrappedAdapter();
        presenter.startCache(HomePagePresenter.CACHE_OBJ, HomePagePresenter.CACHE_OBJ, adapter.getData(), null);
    }

    class MyViewPageAdapter extends PagerAdapter {
        Context context;

        public MyViewPageAdapter(Context context) {
            this.context = context;
        }

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


    class MyOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        /**
         * 下拉刷新时将初始化数据
         */
        @Override
        public void onRefresh() {
            bindData();
        }
    }

    class MyOnScrollListener implements AbsListView.OnScrollListener {
        /**
         * 监听滑动到底部
         *
         * @param absListView
         * @param scrollState
         */
        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            switch (scrollState) {
                // 当不滚动时
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    // 判断滚动到底部
                    if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                        presenter.nextData(new HomePagePresenter.OnGetDataSuccessByNet() {
                            @Override
                            public void onGetData(boolean isSuccessful) {
                                if (isSuccessful) {
                                    cache();
                                } else {
                                }
                            }
                        });
                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        }
    }

    /**
     * 监听网络状态
     */
    class NetWorkManager implements Runnable {
        private boolean isRunning;

        public NetWorkManager() {
            llOffLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    HomeFragment.this.startActivity(intent);
                }
            });
            start();
        }

        public void stop() {
            isRunning = false;
        }

        public void start() {
            isRunning = true;
        }

        @Override
        public void run() {
            while (isRunning) {
                if (presenter.isOnLine()) {
                    handler.sendEmptyMessage(IS_ONLINE);
                } else {
                    handler.sendEmptyMessage(IS_OFFLINE);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

}
