package com.example.heyong.eeyeswindow.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.CustomView.AlwaysMarqueeTextView;
import com.example.heyong.eeyeswindow.UI.Fragment.FindFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by Heyong on 2017/1/7.
 */

public class SearchFragmentHotAdapter extends RecyclerView.Adapter<SearchFragmentHotAdapter.MyViewHolder> {

    Context context;
    List<String> data;

    public SearchFragmentHotAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fragment_search_hot,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Glide.with(context).load(R.drawable.i2)
                .bitmapTransform(new CropCircleTransformation(context))
                .error(R.drawable.ic_insert_photo_black_24dp)
                .into(holder.ivHot);
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "-->" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_hot)
        ImageView ivHot;
        @BindView(R.id.tv_name)
        AlwaysMarqueeTextView tvName;
        @BindView(R.id.card)
        CardView card;
        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }

        public void setOnClickListener(View.OnClickListener listener){
            card.setOnClickListener(listener);
        }
    }
}
