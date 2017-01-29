package com.example.heyong.eeyeswindow.UI.Adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heyong.eeyeswindow.R;
import com.example.heyong.lib.flowLayout.FlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.heyong.eeyeswindow.Tools.PxToDp.dip2px;

/**
 * Created by Heyong on 2017/1/27.
 *
 * 活动详情 讲座详情
 */

public class ActivityDetailViewHolder {

    Context context;
    @BindView(R.id.iv_detail)
    ImageView ivDetail;//顶部图片
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_speech_maker)
    TextView tvSpeechMaker;
    @BindView(R.id.tv_publisher)
    TextView tvPublisher;
    @BindView(R.id.flow_label)
    FlowLayout flowLabel;//承载标签
    @BindView(R.id.tv_content)
    TextView tvContent;

    public ActivityDetailViewHolder(View view,Context context) {
        this.context = context;
        ButterKnife.bind(this,view);
    }


    public void yeildText(List<String> labels){
        for(String s : labels)
            yeildText(s);
    }

    private void yeildText(String text){
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//
        lp.setMargins(dip2px(context, 10), 0, dip2px(context, 10), 0);
        TextView tv = new TextView(context);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setLines(1);
        tv.setBackgroundResource(R.drawable.corner_text_view_background);
        flowLabel.addView(tv, lp);
    }



}
