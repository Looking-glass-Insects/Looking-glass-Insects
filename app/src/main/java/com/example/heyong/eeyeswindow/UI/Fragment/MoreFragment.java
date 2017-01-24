package com.example.heyong.eeyeswindow.UI.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.Tools.CacheUtil;
import com.example.heyong.eeyeswindow.Tools.SimpleDialogFactory;
import com.example.heyong.eeyeswindow.UI.Activity.AboutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreFragment extends Fragment {


    @BindView(R.id.tv_cache_size)
    TextView tvCacheSize;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout rlClearCache;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;

//    static final int CACHE_CLEAN_FINISHED = 2;
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == CACHE_CLEAN_FINISHED) {
//
//            }
//            super.handleMessage(msg);
//        }
//    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        String size = CacheUtil.getCacheSize(getContext());
        tvCacheSize.setText(size);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden)
            tvCacheSize.setText(CacheUtil.getCacheSize(getContext()));
        super.onHiddenChanged(hidden);
    }

    @OnClick(R.id.rl_clear_cache)
    public void onClickCleanCache() {
        SimpleDialogFactory.alertDialog(getContext(), "提示", "确定清空缓存吗？", R.drawable.ic_adb_black_24dp,
                new SimpleDialogFactory.IAlertDialogCallBack() {
                    @Override
                    public void doSomething(boolean isOK) {
                        if (isOK) {
                            CacheUtil.clearAllCache(getContext());
                            tvCacheSize.setText("0kb");
                            Toast.makeText(getContext(), "清理完成", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @OnClick(R.id.rl_about)
    public void onClickAbout() {
        startActivity(new Intent(this.getContext(),AboutActivity.class));
    }
}
