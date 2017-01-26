package com.example.heyong.eeyeswindow.UI.Adapter;

/**
 * Created by Heyong on 2017/1/26.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.heyong.eeyeswindow.Bean.HomeActivityBean;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.Activity.ActivityDetailActivity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class ActivityBeanRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder>{

    public static final String BEAN = "bean";

    Context context;
    List<HomeActivityBean> data = new LinkedList<>();

    public ActivityBeanRecyclerViewAdapter(Context context) {
        this.context = context;
    }
    public void addData(List<HomeActivityBean> list){
        data.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityDetailActivity.class);
                intent.putExtra(BEAN,(Serializable) data.get(position));
                context.startActivity(intent);
            }
        });
        HomeActivityBean bean = data.get(position);
        Glide.with(context).load(bean.getPicURL()).error(R.drawable.ic_insert_photo_black_24dp);
        holder.itemTitle.setText(bean.getTitle());
        holder.tv1.setText(bean.getTv1());
        holder.tv2.setText(bean.getTv2());
        holder.tv3.setText(bean.getTv3());
        holder.tvTime.setText(bean.getTime());
        holder.tvLocation.setText(bean.getLocation());
        holder.tvPublisher.setText(bean.getPublisher());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

//    class ViewHolder extends RecyclerView.ViewHolder{
//        @BindView(R.id.item_home_lecture_photo)
//        ImageView itemHomePhoto;
//        @BindView(R.id.item_home_lecture_title)
//        AlwaysMarqueeTextView itemTitle;
//        @BindView(R.id.tv_1)
//        TextView tv1;
//        @BindView(R.id.tv_2)
//        TextView tv2;
//        @BindView(R.id.tv_3)
//        TextView tv3;
//        @BindView(R.id.tv_time)
//        TextView tvTime;
//        @BindView(R.id.tv_location)
//        TextView tvLocation;
//        @BindView(R.id.tv_publisher)
//        TextView tvPublisher;
//        @BindView(R.id.card)
//        CardView card;
//
//        ViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//
//        public void setOnClickListener(final int position) {
//            card.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, ActivityDetailActivity.class);
//                    intent.putExtra(BEAN,(Serializable) data.get(position));
//                    context.startActivity(intent);
//                }
//            });
//        }
//    }
}
