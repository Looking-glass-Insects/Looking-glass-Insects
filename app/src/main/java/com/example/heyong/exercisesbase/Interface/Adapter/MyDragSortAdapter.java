package com.example.heyong.exercisesbase.Interface.Adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.R;
import com.example.heyong.exercisesbase.Tools.NoForegroundShadowBuilder;


import java.util.List;

import butterknife.BindView;

/**
 *
 */
@Deprecated
public class MyDragSortAdapter extends DragSortAdapter<MyDragSortAdapter.MainViewHolder> {
    private List<Note> data;
    private Context context;
    private static String TAG = "SlidingListAdapter";

    public MyDragSortAdapter(Context context, List<Note> data, RecyclerView recyclerView) {
        super(recyclerView);
        this.context = context;
        this.data = data;
    }

    public void remove(int position) {
        data.remove(position);
    }

    public void remove(Note note) {
        data.remove(note);
    }

    public void add(Note note) {
        data.add(0, note);
        //this.notifyDataSetChanged();
    }


    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.sliding_list_item, parent,
                false);
        MainViewHolder holder = MainViewHolder.getInstance(this,view);
        view.setOnClickListener(holder);
        view.setOnLongClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.text.setText(data.get(position).getQuestion());
        holder.container.setVisibility(getDraggingId() == position ? View.INVISIBLE : View.VISIBLE);
        holder.container.postInvalidate();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getPositionForId(long id) {
        return (int) id;
    }

    @Override
    public boolean move(int fromPosition, int toPosition) {
        data.add(toPosition, data.remove(fromPosition));
        return true;
    }

    static class MainViewHolder extends DragSortAdapter.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        private ViewGroup container;
        private TextView text;
        private static MainViewHolder holder;
        private MainViewHolder(DragSortAdapter adapter, View itemView) {
            super(adapter, itemView);
            container = (ViewGroup)itemView.findViewById(R.id.sliding_container);
            text = (TextView)itemView.findViewById(R.id.textView);
        }
        public static MainViewHolder getInstance(DragSortAdapter adapter,View itemView){
            if(holder == null)return new MainViewHolder(adapter,itemView);
            else return holder;
        }
        @Override
        public void onClick(@NonNull View v) {
            Log.d(TAG, text.getText() + " clicked!");
        }

        @Override
        public boolean onLongClick(@NonNull View v) {
            startDrag();
            return true;
        }

        @Override
        public View.DragShadowBuilder getShadowBuilder(View itemView, Point touchPoint) {
            return new NoForegroundShadowBuilder(itemView, touchPoint);
        }
    }

}

