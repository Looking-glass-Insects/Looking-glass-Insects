package com.example.heyong.eeyeswindow.UI.CustomView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.Tools.PxToDp;
import com.example.heyong.eeyeswindow.UI.Activity.ScanActivity;
import com.example.heyong.eeyeswindow.UI.Activity.SearchActivity;
import com.google.zxing.integration.android.IntentIntegrator;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Heyong
 */

public class SearchPopupWindow extends PopupWindow {
    static public String SUBMIT_TEXT = "submit";

    View menuView;
    @BindView(R.id.image_back)
    ImageView imageBack;

    @BindView(R.id.image_scan)
    ImageView imageScan;

    Activity context;
    @BindView(R.id.sv_search)
    SearchView svSearch;


    public SearchPopupWindow(final Activity context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popup_search, null);
        setContentView(menuView);
        ButterKnife.bind(this, menuView);
        final float scale = context.getResources().getDisplayMetrics().density;
        setHeight(PxToDp.dip2px(context, 56));
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        setWidth(w);

        setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        setAnimationStyle(R.style.MyPopupWindow);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        setOutsideTouchable(true);
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.5f;
        context.getWindow().setAttributes(lp);
        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = context.getWindow().getAttributes();
                lp.alpha = 1f;
                context.getWindow().setAttributes(lp);
            }
        });

        svSearch.onActionViewExpanded();
        svSearch.setOnQueryTextListener(new MyQueryTextListener());
    }


    @OnClick(R.id.image_back)
    public void onClick() {
        dismiss();
    }

    @OnClick(R.id.image_scan)
    public void onClickImageScan() {
        dismiss();
        new IntentIntegrator(context).
                setCaptureActivity(ScanActivity.class).initiateScan();
    }

    class MyQueryTextListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra(SUBMIT_TEXT, query);
            context.startActivity(intent);
            dismiss();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }
}
