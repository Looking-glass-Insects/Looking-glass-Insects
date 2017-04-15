package com.example.heyong.myreader.ui.holder;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heyong.myreader.R;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heyong on 2017/4/7.
 */

public class ItemTitleHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_item_title)
    ImageView ivTitleIcon;
    @BindView(R.id.tv_item_title)
    TextView tvTitle;
    @BindView(R.id.loading_icon)
    AVLoadingIndicatorView loadingIcon;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.ll_next)
    LinearLayout llNext;

    public ItemTitleHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        loadingIcon.setVisibility(View.INVISIBLE);
    }

    public void setOnClickNextListener(View.OnClickListener listener) {
        llNext.setOnClickListener(listener);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitleIcon(Bitmap titleIcon){
        ivTitleIcon.setImageBitmap(titleIcon);
    }
    public void setLoadingIconColor(int color){
        loadingIcon.setIndicatorColor(color);
    }
    public void showIncon(boolean show) {
        if (show) {
            loadingIcon.setVisibility(View.VISIBLE);
        } else {
            loadingIcon.setVisibility(View.INVISIBLE);
        }
    }
}
