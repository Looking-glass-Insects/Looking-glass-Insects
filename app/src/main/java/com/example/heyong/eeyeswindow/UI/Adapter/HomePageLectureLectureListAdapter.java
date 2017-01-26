package com.example.heyong.eeyeswindow.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.heyong.eeyeswindow.Bean.HomeLectureBean;
import com.example.heyong.eeyeswindow.Presenter.HomePageLecturePresenter;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.Activity.LectureDetailActivity;
import com.example.heyong.eeyeswindow.UI.CustomView.AlwaysMarqueeTextView;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heyong
 * <p>
 * 实现了 HomePagePresenter.HomePageDataListener
 */

public class HomePageLectureLectureListAdapter extends BaseAdapter implements HomePageLecturePresenter.HomePageLectureDataListener {
    static String TAG = "HomePage";
    public static final String BEAN = "bean";

    Context context;
    List<HomeLectureBean> data = new LinkedList<>();

    //ListView listView;
    public HomePageLectureLectureListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_home_lecture, null);
        } else {

        }
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setOnClickListener(i);
        //viewHolder.itemHomeLectureTitle.setText(data.get(i).getTitle());
        Glide.with(context).load(data.get(i).getPicURL()).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.ic_insert_photo_black_24dp).into(viewHolder.itemHomeLecturePhoto);
        return view;
    }

    @Override
    public void onGetData(List<HomeLectureBean> beanList) {
        data.addAll(beanList);
        this.notifyDataSetChanged();
    }

    public LinkedList<HomeLectureBean> getData() {
        LinkedList<HomeLectureBean> list = new LinkedList<>();
        list.addAll(data);
        return list;
    }

//    public void setData(List<HomeLectureBean> data) {
//        this.data = data;
//    }



     class ViewHolder {
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
