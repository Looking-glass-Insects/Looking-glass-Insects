package com.example.heyong.exercisesbase.Interface.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.R;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;


public class DragListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Note> data;

    public DragListViewAdapter(Context context, List<Note> data) {
        this.context = context;
        this.data = data;
    }

    public List<Note> getData() {
        return data;
    }

    public void remove(Note note){
        data.remove(note);
        this.notifyDataSetChanged();
    }
    public void clear(){
        data.clear();
        this.notifyDataSetChanged();
    }
    public void switchItem(int from ,int to){
        Collections.swap(data,from,to);
        this.notifyDataSetChanged();
    }
    public Note remove(int position){
        Note n = data.remove(position);
        this.notifyDataSetChanged();
        return n;
    }
    public void add(int position,Note note){
        data.add(position,note);
        this.notifyDataSetChanged();
    }
    public void add(Note note){
        data.add(note);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.dragsort_item,null);
            viewHolder.text = (TextView)convertView.findViewById(R.id.textView2);
            viewHolder.del = (ImageView)convertView.findViewById(R.id.click_remove);
            viewHolder.drag = (ImageView)convertView.findViewById(R.id.drag_handle);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
            viewHolder.text.setText(data.get(position).getQuestion());
        return convertView;
    }
    class ViewHolder{
        ImageView del;
        TextView text;
        ImageView drag;
    }
}
