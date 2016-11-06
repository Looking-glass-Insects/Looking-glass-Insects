package com.example.heyong.exercisesbase.Interface.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.R;

import java.util.List;


/**
 *
 * view  sliding_list_item
 */
public class NoteAdapter extends BaseAdapter {
    private List<Note> notes;
    private Context context;

    public NoteAdapter(Context context,List<Note> notes) {
        this.context = context;

        this.notes = notes;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if(view == convertView){
            view = LayoutInflater.from(context).inflate(R.layout.note_item,null);
        }else{
            view = convertView;
        }
        TextView tv = (TextView)view.findViewById(R.id.noteItem);
        Note n = notes.get(position);
        tv.setText(n.getQuestion());
        return view;
    }
}
