package com.example.heyong.eeyeswindow.UI.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.example.heyong.eeyeswindow.UI.Activity.CallBackActivity;
import com.example.heyong.eeyeswindow.UI.Activity.MoreProductActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreFragment extends Fragment {

    static String TAG = "MoreFragment";
    @BindView(R.id.tv_cache_size)
    TextView tvCacheSize;
    //缓存清理
    @BindView(R.id.rl_clear_cache)
    RelativeLayout rlClearCache;
    //关于
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    //意见反馈
    @BindView(R.id.rl_call_back)
    RelativeLayout rlCallBack;
    //更多产品
    @BindView(R.id.rl_more_production)
    RelativeLayout rlMoreProduction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        try {
            tvCacheSize.setText(CacheUtil.getCacheSize(getContext()));
        } catch (Exception e) {
            Log.d(TAG,"error at onCreateView");
        }
        Log.d(TAG,"onCreateView");
        return view;
    }



    @OnClick(R.id.rl_clear_cache)
    public void onClickCleanCache() {
        SimpleDialogFactory.alertDialog(getContext(), "提示", "确定清空缓存吗？", R.drawable.ic_adb_black_24dp,
                new SimpleDialogFactory.IAlertDialogCallBack() {
                    @Override
                    public void doSomething(boolean isOK) {
                        if (isOK) {
                            CacheUtil.clearAllCache(getContext());
                            tvCacheSize.setText("0.0Byte");
                            Toast.makeText(getContext(), "清理完成", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @OnClick(R.id.rl_about)
    public void onClickAbout() {
        startActivity(new Intent(this.getContext(),AboutActivity.class));
    }

    @OnClick(R.id.rl_call_back)
    public void onClickCallBack(){
        startActivity(new Intent(this.getContext(), CallBackActivity.class));
    }
    @OnClick(R.id.rl_more_production)
    public void onClickMore(){
        startActivity(new Intent(this.getContext(), MoreProductActivity.class));
    }


}
