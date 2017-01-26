package com.example.heyong.eeyeswindow.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heyong.eeyeswindow.Bean.HomeLectureBean;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.Activity.LectureDetailActivity;
import com.example.heyong.eeyeswindow.UI.CustomView.AlwaysMarqueeTextView;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heyong on 2017/1/26.
 */

public class LectureBeanRecyclerViewAdapter extends RecyclerView.Adapter<LectureBeanRecyclerViewAdapter.ViewHolder>{

    public static final String BEAN = "bean";

    Context context;
    List<HomeLectureBean> data = new LinkedList<>();

    public LectureBeanRecyclerViewAdapter(Context context) {
        this.context = context;
    }
    public void setDate(List<HomeLectureBean> list){
        data.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_lecture, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setOnClickListener(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_home_lecture_photo)
        ImageView itemHomeLecturePhoto;
        @BindView(R.id.item_home_lecture_title)
        AlwaysMarqueeTextView itemHomeLectureTitle;
        @BindView(R.id.tv_1)
        TextView tv1;
        @BindView(R.id.tv_2)
        TextView tv2;
        @BindView(R.id.tv_3)
        TextView tv3;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_location)
        TextView tvLocation;
        @BindView(R.id.tv_publisher)
        TextView tvPublisher;
        @BindView(R.id.card)
        CardView card;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setOnClickListener(final int position) {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LectureDetailActivity.class);
                    intent.putExtra(BEAN,(Serializable) data.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }
}
