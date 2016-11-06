package com.example.heyong.exercisesbase.Interface.Listener;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class OnSlidingListItemClicked implements AdapterView.OnItemClickListener {
    Context context;
    public OnSlidingListItemClicked(Context context) {
        this.context = context;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, "onItemClick"+id,
                Toast.LENGTH_SHORT).show();
    }
}
