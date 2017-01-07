package com.example.heyong.eeyeswindow.UI.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.heyong.eeyeswindow.Bean.HomeLectureBean;
import com.example.heyong.eeyeswindow.Presenter.HomePagePresenter;
import com.example.heyong.eeyeswindow.R;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heyong
 *
 * 实现了 HomePagePresenter.HomePageDataListener
 *
 */

public class HomePageLectureListAdapter extends BaseAdapter implements HomePagePresenter.HomePageDataListener{
    static String TAG = "HomePage";
    Context context;
    List<HomeLectureBean> data = new LinkedList<>();

    public HomePageLectureListAdapter(Context context) {
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
        viewHolder.itemHomeLectureTitle.setText(data.get(i).getTitle());
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
        return (LinkedList<HomeLectureBean>)data;
    }

    public void setData(List<HomeLectureBean> data) {
        this.data = data;
    }

    static class ViewHolder {
        @BindView(R.id.item_home_lecture_photo)
        ImageView itemHomeLecturePhoto;
        @BindView(R.id.item_home_lecture_title)
        TextView itemHomeLectureTitle;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
