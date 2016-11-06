package com.example.heyong.exercisesbase.Interface.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.R;

import java.util.ArrayList;
import java.util.List;

public class AddQueAdapter extends BaseAdapter {
    static final String TAG = "AddQueAdapter";
    private List<Note> data;
    private Context context;
    //private List<Note> checkedNotes;
    public boolean[] isPosChecked;
    private CheckBox radioButton;

    public AddQueAdapter(List<Note> data, Context context) {
        this.data = data;
        this.context = context;
        isPosChecked = new boolean[data.size()];
        for (int i = 0; i < isPosChecked.length; i++)
            isPosChecked[i] = false;
        // checkedNotes = new ArrayList<>();
    }

    public void clear() {
        data.clear();
        this.notifyDataSetChanged();
    }

    public void add(List<Note> notes) {
        for (Note n : notes)
            data.add(n);
        isPosChecked = new boolean[data.size()];
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.add_que_item, null);
        } else {
            view = convertView;
        }
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(data.get(position).getQuestion());
        radioButton = (CheckBox) view.findViewById(R.id.radio);
        radioButton.setChecked(isPosChecked[position]);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox)v;
                isPosChecked[position] = cb.isChecked();
            }
        });
        Log.i(TAG,position+"--->"+isPosChecked[position]+"");
        return view;
    }

    public List<Note> getCheckedNotes() {
        List<Note> temp = new ArrayList<>();
        if (data.size() <= 0) return temp;
        for (int i = 0; i < data.size(); i++) {
            if (isPosChecked[i]) {
                temp.add(data.get(i));
            }
        }
        for (int i = 0; i < isPosChecked.length; i++) {
            isPosChecked[i] = false;
        }
        this.radioButton.setChecked(false);
        this.notifyDataSetChanged();
        return temp;
    }

}
