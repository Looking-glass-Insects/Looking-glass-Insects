package com.example.heyong.exercisesbase.Interface.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heyong.exercisesbase.Bean.Bean;
import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.R;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ModelSimpleAdapter extends BaseAdapter {
    private Context context;
    private List<Bean> data;
    static final String TAG = "ModelSimpleAdapter";
    public ModelSimpleAdapter(@Nullable Context context, @Nullable List<Bean> data) {
        this.context = context;
        this.data = data;
    }
    public List<Bean> getData() {
        return data;
    }
    public void updateData(String content,int position){
        data.get(position).setContent(content);
    }
    public void remove(Bean bean){
        data.remove(bean);
        this.notifyDataSetChanged();
    }
    public void clear(){
        data.clear();
        this.notifyDataSetChanged();
    }
    public void switchItem(int from ,int to){
        Collections.swap(data, from, to);
        this.notifyDataSetChanged();
    }
    public Bean remove(int position){
        Bean n = data.remove(position);
        this.notifyDataSetChanged();
        return n;
    }
    public void add(int position,Bean note){
        data.add(position,note);
        this.notifyDataSetChanged();
    }
    public void add(Bean bean){
        data.add(bean);
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.model_list_item,null);
            viewHolder.text = (TextView)convertView.findViewById(R.id.textView2);
            viewHolder.drag = (ImageView)convertView.findViewById(R.id.drag_handle);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        String date = data.get(position).getDate();
        String[] date_title = date.split("&");
        Log.i(TAG,date);
        viewHolder.text.setText(date_title[0]+" "+date_title[1]);
        return convertView;
    }

    class ViewHolder{
        TextView text;
        ImageView drag;
    }
}
