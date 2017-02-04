package com.example.heyong.eeyeswindow.UI.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.Tools.RotateTransformation;
import com.example.heyong.eeyeswindow.UI.CustomView.AlwaysMarqueeTextView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Heyong on 2017/1/26.
 */

public class ItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_photo)
    ImageView itemPhoto;
    @BindView(R.id.item_title)
    AlwaysMarqueeTextView itemTitle;

    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_publisher)
    TextView tvPublisher;
    @BindView(R.id.card)
    CardView card;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.iv_can_print)
    ImageView canPrint;//if 可盖章

    public ItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        Glide.with(itemView.getContext()).load(R.mipmap.can_print).transform(new RotateTransformation(itemView.getContext(),30f)).into(canPrint);
    }


    public void setOnClickListener(View.OnClickListener listener) {
        card.setOnClickListener(listener);
    }
}
