package com.example.heyong.myreader.ui.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heyong.myreader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heyong on 2017/4/7.
 */

public class BeanHolder extends RecyclerView.ViewHolder {
    private Context context;
    View rootView;
    @BindView(R.id.iv_bean)
    ImageView ivPhoto;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_source)
    TextView tvsource;

    public void setContext(@NonNull Context context) {
        this.context = context;
    }

    public BeanHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        ButterKnife.bind(this,itemView);
    }

    public void setPhoto(String url){
        Glide.with(context).load(url).into(ivPhoto);
    }

    public void setDescString(String desc){
        tvDesc.setText(desc);
    }
    public void setSource(String source){
        tvsource.setText(source);
    }

    public void setOnClickListener(View.OnClickListener listener){
        rootView.setOnClickListener(listener);
    }
}
