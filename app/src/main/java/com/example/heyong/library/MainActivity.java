package com.example.heyong.library;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heyong.library.cache.DiskLruCacheHelper;
import com.example.heyong.library.customView.EmptyRecyclerView;
import com.example.heyong.library.customView.swipeBackActivity.SwipeBackActivity;
import com.example.heyong.library.tools.NotificationHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MainActivity extends SwipeBackActivity {
    static String TAG = "MainActivity";

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(MainActivity.this, "fin", Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EmptyRecyclerView rc = (EmptyRecyclerView) findViewById(R.id.rc);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, null, false);
        rc.setAdapter(new MyAdapter());
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setEmptyView(emptyView);


        //addCustomNotification();
        //addSimpleNotification();
        final TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText("123");
       // writeFile();

        //readFile();

        //getSize();
//        DiskLruCacheHelper.init(this);
//        DiskLruCacheHelper.removeAll(new DiskLruCacheHelper.IRemoveListener() {
//            @Override
//            public void onRemoveFin() {
//                handler.sendEmptyMessage(1);
//            }
//        });
    }

    private void getSize() {
        DiskLruCacheHelper.init(this);
        String size = DiskLruCacheHelper.getFormatSize();
        Log.d(TAG, "Size-->" + size);
    }

    private void readFile() {
        final TextView tv = (TextView) findViewById(R.id.tv);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
                DiskLruCacheHelper.init(MainActivity.this);
                DiskLruCacheHelper.getCache(new DiskLruCacheHelper.ReadCallBack() {
                    @Override
                    public String dir() {
                        return "s";
                    }

                    @Override
                    public String key() {
                        return "123";
                    }

                    @Override
                    public void onGetInputStream(InputStream is) {
                        try {
                            ObjectInputStream ois = new ObjectInputStream(is);
                            String s = (String) ois.readObject();
                            Log.d(TAG, "onGetInputStream->" + s);
                            tv.setText(s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    private void writeFile() {
        DiskLruCacheHelper.init(this);
        DiskLruCacheHelper.enqueue(new DiskLruCacheHelper.WriteCallBack() {
            @Override
            public String dir() {
                return "s";
            }

            @Override
            public String key() {
                return "123";
            }

            @Override
            public long maxSize() {
                return 0;
            }

            @Override
            public boolean onGetStream(OutputStream os) {
                if (os == null) {
                    Log.d(TAG, "os is null");
                    return false;
                }
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
                    objectOutputStream.writeObject("hello world");
                    objectOutputStream.flush();
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
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
                        remoteViews.setTextViewText(R.id.tv, "hello");
                        remoteViews.setOnClickPendingIntent(R.id.iv, PendingIntent.getActivity(
                                MainActivity.this,
                                0,
                                new Intent(MainActivity.this, MainActivity.class),
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


    class MyAdapter extends RecyclerView.Adapter {

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
