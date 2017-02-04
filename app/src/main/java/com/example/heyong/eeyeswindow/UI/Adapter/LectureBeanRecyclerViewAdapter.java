package com.example.heyong.eeyeswindow.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.heyong.eeyeswindow.Bean.HomeLectureBean;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.Activity.LectureDetailActivity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Heyong on 2017/1/26.
 */

public class LectureBeanRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder>{

    public static final String BEAN = "bean";

    Context context;
    List<HomeLectureBean> data = new LinkedList<>();

    public LectureBeanRecyclerViewAdapter(Context context) {
        this.context = context;
    }
    public void addData(List<HomeLectureBean> list){
        data.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder,final int position) {
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LectureDetailActivity.class);
                intent.putExtra(BEAN,(Serializable) data.get(position));
                context.startActivity(intent);
            }
        });
        HomeLectureBean bean = data.get(position);

        holder.itemTitle.setText(bean.getTitle());
        holder.tv1.setText(bean.getTv1());
        holder.tv2.setText(bean.getTv2());
        holder.tv3.setText(bean.getTv3());
        holder.tvTime.setText(bean.getTime());
        holder.tvLocation.setText(bean.getLocation());
        holder.tvPublisher.setText(bean.getPublisher());
        Glide.with(context).load(bean.getPicURL()).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.ic_insert_photo_black_24dp).into(holder.itemPhoto);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

}
