package com.example.heyong.eeyeswindow.UI.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.Tools.PxToDp;
import com.example.heyong.eeyeswindow.Tools.RotateTransformation;
import com.example.heyong.eeyeswindow.UI.CustomView.pulltozoomview.PullToZoomScrollView;
import com.example.heyong.lib.flowLayout.FlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.heyong.eeyeswindow.Tools.PxToDp.dip2px;

/**
 * Created by Heyong on 2017/1/27.
 * <p>
 * 活动详情 讲座详情
 */

public class ActivityDetailViewHolder {

    Context context;
//    @BindView(R.id.iv_detail_banner)
//    ImageView ivDetailBanner;
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
    FlowLayout flowLabel;
    @BindView(R.id.tv_content_title)
    TextView tvContentTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_can_print)
    ImageView ivCanPrint;

    LinearLayout container;


    //private ImageView ivDetailBanner;

    private PullToZoomScrollView scrollView;

    public ActivityDetailViewHolder(View view, Context context) {
        this.context = context;
        scrollView = (PullToZoomScrollView) view.findViewById(R.id.zoom_scroll_view);
        View contentView = LayoutInflater.from(context).inflate(R.layout.activity_detail_content,null,false);
        container = (LinearLayout) view.findViewById(R.id.container);

        ButterKnife.bind(this, contentView);
        Glide.with(context).load(R.mipmap.can_print).bitmapTransform(new RotateTransformation(context,30))
                .into(ivCanPrint);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PxToDp.dip2px(context,200) );
        scrollView.setHeaderLayoutParams(layoutParams);
        scrollView.setScrollContentView(contentView);
    }





    //是否显示可盖章的图片
    public void canPrint(boolean canPrint) {
        int FLAG = canPrint ? View.VISIBLE : View.GONE;
        ivCanPrint.setVisibility(FLAG);
    }
    //添加标签
    public void yeildText(List<String> labels) {
        for (String s : labels)
            yeildText(s);
    }
    //设置标题
    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setTime(String time){
        tvTime.setText(time);
    }

    public void setTvLocation(String location){
        tvLocation.setText(location);
    }

    //演讲人
    public void setSpeechMaker(String speechMaker){
        tvSpeechMaker.setText(speechMaker);
    }
    //发起方
    public void setPublisher(String publisher){
        tvPublisher.setText(publisher);
    }
    public void setContentTitle(String contentTitle){
        tvContentTitle.setText(contentTitle);
    }
    public void setContent(String content){
        tvContent.setText(content);
    }

    public void setBannerImage(String url){
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(url).asBitmap().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                Palette.Builder builder = Palette.from(resource);
                builder.generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch swatch = palette.getLightVibrantSwatch();
                        container.setBackgroundColor(swatch.getRgb());
                    }
                });
                return false;
            }
        }).into(imageView);
        scrollView.setZoomView(imageView);
    }

    public void setBannerImage(int res){
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(res).asBitmap().listener(listener).into(imageView);
        scrollView.setZoomView(imageView);
    }


    private final  RequestListener<Integer, Bitmap> listener = new RequestListener<Integer, Bitmap>() {
        @Override
        public boolean onException(Exception e, Integer model, Target<Bitmap> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Bitmap resource, Integer model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
            Palette.Builder builder = Palette.from(resource);
            builder.generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch swatch = palette.getLightVibrantSwatch();
                    container.setBackgroundColor(swatch.getRgb());
                }
            });
            return false;
        }
    };



    @SuppressWarnings("all")
    private void yeildText(String text) {
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
