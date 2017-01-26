package com.example.heyong.eeyeswindow.UI.Fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.heyong.eeyeswindow.Presenter.HomePageActivityPresenter;
import com.example.heyong.eeyeswindow.Presenter.HomePageLecturePresenter;
import com.example.heyong.eeyeswindow.Presenter.OnGetDataSuccessByNet;
import com.example.heyong.eeyeswindow.Presenter.Presenter;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.Receiver.NetworkReceiver;
import com.example.heyong.eeyeswindow.UI.Adapter.HomePageActivityAdapter;
import com.example.heyong.eeyeswindow.UI.Adapter.HomePageLectureLectureListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by HeYong
 */

public class HomeFragment extends Fragment {
    static String TAG = "HomeFragment";
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.vp_view)
    ViewPager viewPager;
    MyViewPageAdapter adapter;

    String[] titles = {"讲座", "活动"};
    View[] views = {
            null, null
    };//thisView pager 视图


    /**
     * view[0]
     */
    ListView lvHomeLecture;
    SwipeRefreshLayout srlHomeLecture;
    View footerLecture;
    HomePageLecturePresenter presenterLecture;//数据提供
    /**
     * view[1]
     */
    ListView lvHomeActivity;
    SwipeRefreshLayout srlHomeActivity;
    View footerActivity;
    HomePageActivityPresenter presenterActivity;


    /**
     * 网络状态
     */
    @BindView(R.id.ll_off_line)
    LinearLayout llOffLine;

    NetworkReceiver receiver;

    public static final int IS_ONLINE = 0;
    public static final int IS_OFFLINE = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            if (msg.what == IS_ONLINE) {
//                llOffLine.setVisibility(View.GONE);
//                footerLecture.setVisibility(View.VISIBLE);
//            } else if (msg.what == IS_OFFLINE) {
//                footerLecture.setVisibility(View.GONE);
//                llOffLine.setVisibility(View.VISIBLE);
//            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        views[0] = LayoutInflater.from(this.getActivity()).inflate(R.layout.fragment_home_pager_view_1, null);
        views[1] = LayoutInflater.from(this.getActivity()).inflate(R.layout.fragment_home_pager_view_1, null);
        //thisView[0] init
        initView0();
        //thisView[1] init
        initView1();


        registerReceiver();
        bindData(0);//耗时操作
        bindData(1);
        adapter = new MyViewPageAdapter();
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    private void initView1() {
        lvHomeActivity = (ListView) views[1].findViewById(R.id.lv_home_lecture);
        View footerParent = LayoutInflater.from(this.getActivity()).inflate(R.layout.fragment_home_footer_load_more, null);
        lvHomeActivity.addFooterView(footerParent);
        footerActivity = footerParent.findViewById(R.id.footer);
        srlHomeActivity = (SwipeRefreshLayout) views[1].findViewById(R.id.srl_home_1);
        srlHomeActivity.setColorSchemeResources(R.color.colorPrimary);
        srlHomeActivity.setOnRefreshListener(new MyOnRefreshListener(1));
        FloatingActionButton btn = (FloatingActionButton) views[1].findViewById(R.id.btn_top);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTop1();
            }
        });
        YoYo.with(Techniques.FadeOutRight).duration(500).playOn(btn);
    }

    private void initView0() {
        lvHomeLecture = (ListView) views[0].findViewById(R.id.lv_home_lecture);
        View footerParent = LayoutInflater.from(this.getActivity()).inflate(R.layout.fragment_home_footer_load_more, null);
        lvHomeLecture.addFooterView(footerParent);
        footerLecture = footerParent.findViewById(R.id.footer);
        srlHomeLecture = (SwipeRefreshLayout) views[0].findViewById(R.id.srl_home_1);
        srlHomeLecture.setColorSchemeResources(R.color.colorPrimary);
        srlHomeLecture.setOnRefreshListener(new MyOnRefreshListener(0));
        FloatingActionButton btn = (FloatingActionButton) views[0].findViewById(R.id.btn_top);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTop0();
            }
        });
        YoYo.with(Techniques.FadeOutRight).duration(500).playOn(btn);
    }

    private View thisView;//防止viewpager 老调该方法造成一些问题

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (thisView != null)
            return thisView;
        thisView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, thisView);
        llOffLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        tabs.addTab(tabs.newTab().setText(titles[0]));
        tabs.addTab(tabs.newTab().setText(titles[1]));
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        return thisView;
    }

    @Override
    public void onDestroy() {
        this.getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }


    public boolean goTop0() {
        if (lvHomeLecture.getFirstVisiblePosition() != 0) {
            //返回顶部
            lvHomeLecture.smoothScrollToPosition(0);
            return true;
        } else {
            return false;
        }
    }
    public boolean goTop1() {
        if (lvHomeActivity.getFirstVisiblePosition() != 0) {
            //返回顶部
            lvHomeActivity.smoothScrollToPosition(0);
            return true;
        } else {
            return false;
        }
    }


    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.receiver = new NetworkReceiver(handler);
        this.getActivity().registerReceiver(this.receiver, filter);
    }

    /**
     * 初始化时调用
     * presenterLecture 和 listView 的 adapter 将会刷新
     */
    public void bindData(int index) {
        if (index == 0) {
            srlHomeLecture.setRefreshing(true);
            HomePageLectureLectureListAdapter adapter = new HomePageLectureLectureListAdapter(getContext());
            presenterLecture = new HomePageLecturePresenter(getContext(), adapter);
            lvHomeLecture.setAdapter(adapter);
            lvHomeLecture.setOnScrollListener(new MyOnScrollListener(lvHomeLecture,presenterLecture ,0));
            presenterLecture.nextData(new OnGetDataSuccessByNet() {
                @Override
                public void onGetData(boolean isSuccessful) {
                    if (isSuccessful) {
                        srlHomeLecture.setRefreshing(false);
                    }
                }
            }, 10);
        } else {
            srlHomeActivity.setRefreshing(true);
            HomePageActivityAdapter activityAdapter = new HomePageActivityAdapter(getContext());
            presenterActivity = new HomePageActivityPresenter(getContext(),activityAdapter);
            lvHomeActivity.setAdapter(activityAdapter);
            lvHomeActivity.setOnScrollListener(new MyOnScrollListener(lvHomeActivity,presenterActivity,1));
            presenterActivity.nextData(new OnGetDataSuccessByNet() {
                @Override
                public void onGetData(boolean isSuccessful) {
                    if(isSuccessful)
                        srlHomeActivity.setRefreshing(false);
                }
            },10);
        }
    }

    /**
     * 缓存
     */
    private void cache() {
        HomePageLectureLectureListAdapter adapter = (HomePageLectureLectureListAdapter) ((HeaderViewListAdapter) lvHomeLecture.getAdapter()).getWrappedAdapter();
        presenterLecture.startCache(adapter.getData());
    }


    class MyViewPageAdapter extends PagerAdapter {

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
        private int index;

        public MyOnRefreshListener(int index) {
            this.index = index;
        }

        /**
         * 下拉刷新时将初始化数据
         */
        @Override
        public void onRefresh() {
            bindData(index);
        }
    }

    class MyOnScrollListener implements AbsListView.OnScrollListener {
        private boolean scrollFlag = false;// 标记是否滑动
        private int lastVisibleItemPosition;// 标记上次滑动位置
        private boolean btnIsVisitable = false;
        private ListView listView;
        private Presenter presenter;
        private int index;

        public MyOnScrollListener(ListView listView,Presenter presenter ,int index) {
            this.listView = listView;
            this.index = index;
            bindPresneter(presenter);
        }

        public void bindPresneter(Presenter presenter){
            this.presenter = presenter;
        }

        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            switch (scrollState) {
                // 当不滚动时
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    scrollFlag = false;
                    // 判断滚动到底部
                    if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                        if (presenter == null)
                            throw new IllegalStateException("presenter is null");
                        presenter.nextData(new OnGetDataSuccessByNet() {
                            @Override
                            public void onGetData(boolean isSuccessful) {

                            }
                        });
                    }
                    FloatingActionButton btn = (FloatingActionButton) views[index].findViewById(R.id.btn_top);
                    if (listView.getFirstVisiblePosition() != 0 && !btnIsVisitable) {
                        YoYo.with(Techniques.FadeInRight).duration(500).playOn(btn);
                        btnIsVisitable = true;
                    } else if (listView.getFirstVisiblePosition() == 0 && btnIsVisitable) {
                        YoYo.with(Techniques.FadeOutRight).duration(500).playOn(btn);
                        btnIsVisitable = false;
                    }
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    scrollFlag = true;
                    break;
                default:
                    scrollFlag = false;
            }


        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (scrollFlag) {
                if (firstVisibleItem > lastVisibleItemPosition) {
                    //Log.e(TAG, "上滑");
//                    MainActivity activity = (MainActivity)HomeFragment.this.getActivity();
//                    AppBarLayout bar = (AppBarLayout)activity.findViewById(R.id.appbar);
//                    bar.setVisibility(View.INVISIBLE);

                }
                if (firstVisibleItem < lastVisibleItemPosition) {
                    //Log.e(TAG, "下滑");
//                    MainActivity activity = (MainActivity)HomeFragment.this.getActivity();
//                    AppBarLayout bar = (AppBarLayout)activity.findViewById(R.id.appbar);
//                    bar.setVisibility(View.VISIBLE);
                }
                if (firstVisibleItem == lastVisibleItemPosition) {
                    return;
                }
                lastVisibleItemPosition = firstVisibleItem;
            }
        }
    }

}
