package com.example.heyong.eeyeswindow.UI.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
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

import com.bumptech.glide.Glide;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.Activity.SearchActivity;
import com.example.heyong.eeyeswindow.UI.Adapter.SearchFragmentHotAdapter;
import com.example.heyong.eeyeswindow.UI.CustomView.FlowLayout;
import com.example.heyong.eeyeswindow.UI.CustomView.SearchPopupWindow;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.example.heyong.eeyeswindow.Tools.PxToDp.dip2px;

/**
 *
 *
 */
public class FindFragment extends Fragment {
    public static final String SEARCH = SearchPopupWindow.SUBMIT_TEXT;

    @BindView(R.id.flowlayout)
    FlowLayout flowlayout;

    FlowLayoutManager flowLayoutManager;

    @BindView(R.id.rc_search_container)
    RecyclerView rcSearchContainer;

    HotPublisherManager publisherManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        flowLayoutManager = new FlowLayoutManager(view);
        publisherManager = new HotPublisherManager();
        init();
        return view;
    }

    private void init() {
    }


    class FlowLayoutManager {
        static final int DEFAULT_SIZE = 9;


        private LinearLayout llSeeMore;
        private LinearLayout llSeeLess;

        final String[] strings = {
                "The",
                "life",
                "is like",
                "a box of",
                "chocolates,",
                "you could not",
                "know",
                "what you're going",
                "to",
                "get.",
                "Victory",
                "won't",
                "come to me",
                "unless",
                "I go to it"
        };

        public FlowLayoutManager(View view) {
            llSeeMore = (LinearLayout) view.findViewById(R.id.ll_see_more);
            llSeeLess = (LinearLayout) view.findViewById(R.id.ll_see_less);
            llSeeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAll();
                    convertVisit();
                }
            });
            llSeeLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        flowlayout.removeViews(DEFAULT_SIZE, strings.length - DEFAULT_SIZE);
                    } catch (Exception e) {
                    }
                    convertVisit();
                }
            });
            this.init();
        }

        private void init() {
            for (int i = 0; i < DEFAULT_SIZE; i++)
                yieldText(strings[i]);
            flowlayout.relayoutToCompress();
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
            for (String s : strings) {
                yieldText(s);
            }
            flowlayout.relayoutToCompress();
        }

        private void yieldText(final String text) {
            int ranHeight = dip2px(FindFragment.this.getContext(), 30);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(WRAP_CONTENT, ranHeight);
            lp.setMargins(dip2px(FindFragment.this.getContext(), 10), 0, dip2px(FindFragment.this.getContext(), 10), 0);
            TextView tv = new TextView(FindFragment.this.getContext());
            tv.setPadding(dip2px(FindFragment.this.getContext(), 15), 0, dip2px(FindFragment.this.getContext(), 15), 0);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            //int index = (int)(Math.random() * length);
            tv.setText(text);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setLines(1);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FindFragment.this.getContext(), SearchActivity.class);
                    intent.putExtra(SEARCH, text);
                    startActivity(intent);
                }
            });
            tv.setBackgroundResource(R.drawable.flow_bg);
            flowlayout.addView(tv, lp);
        }

    }

    class HotPublisherManager {

        public HotPublisherManager(){
            List<String> data = new LinkedList<>();
            data.add("1");
            data.add("2");
            data.add("3");
            data.add("4");
            data.add("5");
            data.add("6");
            SearchFragmentHotAdapter adapter = new SearchFragmentHotAdapter(getContext(),data);
            rcSearchContainer.setLayoutManager(new LinearLayoutManager(getContext()));
            rcSearchContainer.setAdapter(adapter);
            rcSearchContainer.setItemAnimator(new DefaultItemAnimator());
            rcSearchContainer.setNestedScrollingEnabled(false);
        }



    }
}
