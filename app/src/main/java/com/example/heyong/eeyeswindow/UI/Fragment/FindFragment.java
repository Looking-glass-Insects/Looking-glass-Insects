package com.example.heyong.eeyeswindow.UI.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heyong.eeyeswindow.Bean.HotPublisherBean;
import com.example.heyong.eeyeswindow.Presenter.FindPagePresenter;
import com.example.heyong.eeyeswindow.Presenter.INetworkCallBack;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.Activity.SearchActivity;
import com.example.heyong.eeyeswindow.UI.Adapter.SearchFragmentHotAdapter;
import com.example.heyong.lib.flowLayout.FlowLayout;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.heyong.eeyeswindow.Tools.PxToDp.dip2px;
import static com.example.heyong.eeyeswindow.UI.CustomView.SearchDialog.SUBMIT_TEXT;

/**
 *
 *
 */
public class FindFragment extends Fragment {
    public static final String SEARCH = SUBMIT_TEXT;
    static String TAG = "FindFragment";
    @BindView(R.id.flowlayout)
    FlowLayout flowlayout;


    @BindView(R.id.rc_search_container)
    RecyclerView rcSearchContainer;

    HotPublisherManager publisherManager;
    FlowLayoutManager flowLayoutManager;
    FindPagePresenter presenter;


    @BindView(R.id.search_root)
    NestedScrollView root;

    @BindView(R.id.iv_change_layout)
    ImageView ivChangeLayout;
    DataHolder dataHolder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences sp = getActivity().getSharedPreferences(STATE, Context.MODE_PRIVATE);
        this.i = sp.getInt(I, 0) % 2;
        dataHolder = new DataHolder();
        presenter = new FindPagePresenter(this.getContext(), dataHolder);
        presenter.nextData(new INetworkCallBack() {
            @Override
            public void onGetData(int code) {

            }
        });
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        publisherManager = new HotPublisherManager();
        flowLayoutManager = new FlowLayoutManager(view);
        dataHolder.bindData();
        init();
        return view;
    }

    private void init() {
        setStateByI();
    }

    @Override
    public void onDestroy() {
        SharedPreferences sp = getActivity().getSharedPreferences(STATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(I, this.i);
        editor.apply();
        super.onDestroy();
    }


    private static final String STATE = "state";
    private static final String I = "i";
    private int i = 0;
    private int[] img_id = {R.drawable.ic_view_list_black_24dp, R.drawable.ic_view_module_black_24dp};
    private RecyclerView.LayoutManager[] managers = {
            new LinearLayoutManager(getContext()),
            new GridLayoutManager(getContext(), 2)
    };

    @OnClick(R.id.iv_change_layout)
    public void onClick() {
        i++;
        setStateByI();
    }

    private void setStateByI() {
        ivChangeLayout.setImageResource(img_id[i % 2]);
        publisherManager.changeLayoutManager(managers[i % 2]);
    }

    class DataHolder implements FindPagePresenter.FindPageDataListener {
        List<String> flowList;
        List<HotPublisherBean> beans;
        /**
         * 回调
         * @param flow
         * @param beans
         */
        @Override
        public void onGetData(List<String> flow, List<HotPublisherBean> beans) {
            this.flowList = flow;
            this.beans = beans;
        }

        public void bindData() {
            publisherManager.setData(beans);
            flowLayoutManager.setData(flowList);
        }
    }

    class FlowLayoutManager {
        static final int DEFAULT_SIZE = 9;


        private LinearLayout llSeeMore;
        private LinearLayout llSeeLess;
        private NestedScrollView flowNestedScrollView;
        private List<String> flowList;

        public FlowLayoutManager(View view) {
            llSeeMore = (LinearLayout) view.findViewById(R.id.ll_see_more);
            llSeeLess = (LinearLayout) view.findViewById(R.id.ll_see_less);
            flowNestedScrollView = (NestedScrollView) view.findViewById(R.id.flow_nested_scroll_view);
        }


        public void setData(final List<String> flowList) {
            this.flowList = flowList;
            llSeeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(getContext(), 200));
                    flowNestedScrollView.setLayoutParams(layoutParams);
                    addAll();
                    convertVisit();
                }
            });
            llSeeLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        flowNestedScrollView.setLayoutParams(layoutParams);
                        flowlayout.removeViews(DEFAULT_SIZE, flowList.size() - DEFAULT_SIZE);
                    } catch (Exception e) {
                    }
                    convertVisit();
                }
            });
            this.init();
        }

        private void init() {
            for (int i = 0; i < DEFAULT_SIZE; i++)
                yieldText(flowList.get(i));
        }

        private void convertVisit() {
            int v = llSeeMore.getVisibility();
            if (v == View.VISIBLE) {
                llSeeMore.setVisibility(View.GONE);
                llSeeLess.setVisibility(View.VISIBLE);
            } else {
                llSeeMore.setVisibility(View.VISIBLE);
                llSeeLess.setVisibility(View.GONE);
            }
        }

        private void addAll() {
            flowlayout.removeAllViews();
            for (String s : flowList)
                yieldText(s);
        }

        private void yieldText(final String text) {
            int ranHeight = dip2px(getContext(), 30);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ranHeight);//
            lp.setMargins(dip2px(getContext(), 10), 0, dip2px(getContext(), 10), 0);
            TextView tv = new TextView(getContext());
            tv.setPadding(dip2px(getContext(), 15), 0, dip2px(getContext(), 15), 0);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            //int index = (int)(Math.random() * length);
            tv.setText(text);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setLines(1);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    intent.putExtra(SEARCH, text);
                    startActivity(intent);
                }
            });
            tv.setBackgroundResource(R.drawable.flow_bg);
            flowlayout.addView(tv, lp);
        }

    }

    class HotPublisherManager {
        SearchFragmentHotAdapter adapter;

        public HotPublisherManager() {
            adapter = new SearchFragmentHotAdapter(getContext());
            rcSearchContainer.setLayoutManager(new LinearLayoutManager(getContext()));
            rcSearchContainer.setAdapter(adapter);
            rcSearchContainer.setItemAnimator(new DefaultItemAnimator());
            rcSearchContainer.setNestedScrollingEnabled(false);
        }

        public void changeLayoutManager(RecyclerView.LayoutManager manager) {
            rcSearchContainer.setLayoutManager(manager);
        }


        public void setData(List<HotPublisherBean> beans) {
            adapter.setData(beans);
        }
    }
}
