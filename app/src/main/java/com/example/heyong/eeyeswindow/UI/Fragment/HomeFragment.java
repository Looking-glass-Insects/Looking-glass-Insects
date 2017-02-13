package com.example.heyong.eeyeswindow.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.heyong.eeyeswindow.Presenter.HomePageActivityPresenter;
import com.example.heyong.eeyeswindow.Presenter.HomePageLecturePresenter;
import com.example.heyong.eeyeswindow.Presenter.INetworkCallBack;
import com.example.heyong.eeyeswindow.Presenter.Presenter;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.Adapter.HomePageActivityAdapter;
import com.example.heyong.eeyeswindow.UI.Adapter.HomePageLectureAdapter;

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
    };//View pager 视图

    static final String FOOTER_FINISH = "已经倒底了_ (:з」∠)_";
    static final String FOOTER_LOADING = "正在加载中(๑•̀ㅂ•́)و✧";
    /**
     * view[0] 讲座对应的页面
     */
    ListView lvHomeLecture;
    SwipeRefreshLayout srlHomeLecture;
    TextView footerLecture;
    HomePageLecturePresenter presenterLecture;//数据提供
    HomePageLectureAdapter lectureAdapter;
    /**
     * view[1] 活动对应的页面
     */
    ListView lvHomeActivity;
    SwipeRefreshLayout srlHomeActivity;
    TextView footerActivity;
    HomePageActivityPresenter presenterActivity;
    HomePageActivityAdapter activityAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        views[0] = LayoutInflater.from(this.getActivity()).inflate(R.layout.fragment_home_pager_view, null);
        views[1] = LayoutInflater.from(this.getActivity()).inflate(R.layout.fragment_home_pager_view, null);
        //thisView[0] init
        initViewLecture();
        //thisView[1] init
        initViewActivity();

        //registerReceiver();
        bindData(0);//耗时操作
        bindData(1);
        adapter = new MyViewPageAdapter();
        //Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    private void initViewActivity() {
        activityAdapter = new HomePageActivityAdapter(this.getContext());
        lvHomeActivity = (ListView) views[1].findViewById(R.id.lv_home_lecture);
        View footerParent = LayoutInflater.from(this.getActivity()).inflate(R.layout.fragment_home_footer, null);
        lvHomeActivity.addFooterView(footerParent);
        View emptyView = views[1].findViewById(R.id.empty);
        lvHomeActivity.setEmptyView(emptyView);
        lvHomeActivity.setAdapter(activityAdapter);
        footerActivity = (TextView) footerParent.findViewById(R.id.footer);
        srlHomeActivity = (SwipeRefreshLayout) views[1].findViewById(R.id.srl_home_1);
        srlHomeActivity.setColorSchemeResources(R.color.colorPrimary);
        srlHomeActivity.setOnRefreshListener(new MyOnRefreshListener(1));
        FloatingActionButton btn = (FloatingActionButton) views[1].findViewById(R.id.btn_top);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTop(lvHomeActivity);
            }
        });
        YoYo.with(Techniques.FadeOutRight).duration(500).playOn(btn);
    }

    private void initViewLecture() {
        lectureAdapter =  new HomePageLectureAdapter(this.getContext());
        lvHomeLecture = (ListView) views[0].findViewById(R.id.lv_home_lecture);
        View footerParent = LayoutInflater.from(this.getActivity()).inflate(R.layout.fragment_home_footer, null, false);
        lvHomeLecture.addFooterView(footerParent);
        View emptyView = views[0].findViewById(R.id.empty);
        lvHomeLecture.setEmptyView(emptyView);
        lvHomeLecture.setAdapter(lectureAdapter);
        footerLecture = (TextView) footerParent.findViewById(R.id.footer);
        srlHomeLecture = (SwipeRefreshLayout) views[0].findViewById(R.id.srl_home_1);
        srlHomeLecture.setColorSchemeResources(R.color.colorPrimary);
        srlHomeLecture.setOnRefreshListener(new MyOnRefreshListener(0));
        FloatingActionButton btn = (FloatingActionButton) views[0].findViewById(R.id.btn_top);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTop(lvHomeLecture);
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
        tabs.addTab(tabs.newTab().setText(titles[0]));
        tabs.addTab(tabs.newTab().setText(titles[1]));
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        return thisView;
    }

    public boolean goTop(ListView listView) {
        if (listView.getFirstVisiblePosition() != 0) {
            //返回顶部
            listView.smoothScrollToPosition(0);
            return true;
        } else {
            return false;
        }
    }


    /**
     * 初始化时调用
     * presenter将会刷新
     */
    public void bindData(int index) {
        if (index == 0) {
            srlHomeLecture.setRefreshing(true);
            footerLecture.setText(FOOTER_LOADING);
            bindLectureData();
        } else {
            srlHomeActivity.setRefreshing(true);
            footerActivity.setText(FOOTER_LOADING);
            bindActivityData();
        }
    }

    private void bindLectureData(){
        lectureAdapter.clearAll();
        presenterLecture = new HomePageLecturePresenter(getContext(), lectureAdapter);
        lvHomeLecture.setOnScrollListener(new MyOnScrollListener(lvHomeLecture, presenterLecture,footerLecture ,0));
        presenterLecture.nextData(new INetworkCallBack() {
            @Override
            public void onGetData(int code) {
                if (code == INetworkCallBack.SUCCESS) {
                    srlHomeLecture.setRefreshing(false);
                }else if(code == INetworkCallBack.DATA_FINISH){
                    srlHomeLecture.setRefreshing(false);
                    footerLecture.setText(FOOTER_FINISH);
                }
            }
        }, 10);
    }
    private void bindActivityData(){
        activityAdapter.clearAll();
        presenterActivity = new HomePageActivityPresenter(getContext(), activityAdapter);
        lvHomeActivity.setOnScrollListener(new MyOnScrollListener(lvHomeActivity, presenterActivity, footerActivity ,1));
        presenterActivity.nextData(new INetworkCallBack() {
            @Override
            public void onGetData(int code) {
                if (code == INetworkCallBack.SUCCESS) {
                    srlHomeActivity.setRefreshing(false);
                }else if(code == INetworkCallBack.DATA_FINISH){
                    srlHomeActivity.setRefreshing(false);
                    footerActivity.setText(FOOTER_FINISH);
                }
            }
        }, 0);
    }

    /**
     * 缓存
     */

    private void cache() {
        HomePageLectureAdapter adapter = (HomePageLectureAdapter) ((HeaderViewListAdapter) lvHomeLecture.getAdapter()).getWrappedAdapter();
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
        private TextView footer;

        public MyOnScrollListener(ListView listView, Presenter presenter,TextView footer ,int index) {
            this.listView = listView;
            this.index = index;
            this.footer = footer;
            bindPresneter(presenter);
        }



        public void bindPresneter(Presenter presenter) {
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
                        presenter.nextData(new INetworkCallBack() {
                            @Override
                            public void onGetData(int code) {
                                if (code == INetworkCallBack.DATA_FINISH) {
                                    footer.setText(FOOTER_FINISH);
                                }
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
