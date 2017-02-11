package com.example.heyong.library;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.example.heyong.library.customView.EmptyRecyclerView;
import com.example.heyong.library.customView.swipeBackActivity.SwipeBackActivity;
import com.example.heyong.library.tools.NotificationHelper;

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


        //addCustomNotification();
        addSimpleNotification();


    }

    private void addSimpleNotification() {
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationHelper helper = new NotificationHelper(MainActivity.this);
                helper.sendSimpleNotification(new NotificationHelper.SimpleCallBack() {
                    @Override
                    public String getContentTitle() {
                        return "hello title";
                    }

                    @Override
                    public String getContentText() {
                        return "hello text";
                    }

                    @Override
                    public int getSmallIconRes() {
                        return R.mipmap.ic_launcher;
                    }

                    @Override
                    public PendingIntent getPendingIntent() {
                        return null;
                    }

                    @Override
                    public int getId() {
                        return 1;
                    }

                    @Override
                    public void beforeNotificationBuild(NotificationCompat.Builder builder) {
                        builder.setAutoCancel(true);
                    }
                });
            }
        });
    }

    private void addCustomNotification() {
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationHelper helper = new NotificationHelper(MainActivity.this);
                helper.sendCustomNotification(new NotificationHelper.CallBack() {
                    @Override
                    public int getLayoutRes() {
                        return R.layout.empty_view;
                    }

                    @Override
                    public int getSmallIconRes() {
                        return R.mipmap.ic_launcher;
                    }

                    @Override
                    public int getId() {
                        return 0;
                    }

                    @Override
                    public void onGetRemoteViews(RemoteViews remoteViews) {
                        remoteViews.setTextViewText(R.id.tv,"hello");
                        remoteViews.setOnClickPendingIntent(R.id.iv, PendingIntent.getActivity(
                                MainActivity.this,
                                0,
                                new Intent(MainActivity.this,MainActivity.class),
                                PendingIntent.FLAG_UPDATE_CURRENT));
                    }

                    @Override
                    public void beforeNotificationBuild(NotificationCompat.Builder builder) {
                        builder.setOngoing(true);
                    }
                });
            }
        });
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
