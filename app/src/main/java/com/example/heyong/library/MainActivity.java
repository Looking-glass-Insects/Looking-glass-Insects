package com.example.heyong.library;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heyong.library.customView.EmptyRecyclerView;
import com.example.heyong.library.customView.swipeBackActivity.SwipeBackActivity;

public class MainActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EmptyRecyclerView rc = (EmptyRecyclerView) findViewById(R.id.rc);
        View emptyView  = LayoutInflater.from(this).inflate(R.layout.empty_view,null,false);
        rc.setAdapter(new MyAdapter());
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setEmptyView(emptyView);
    }


    class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
