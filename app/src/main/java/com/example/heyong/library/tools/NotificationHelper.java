package com.example.heyong.library.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;


public class NotificationHelper {
    protected Context context;
    protected NotificationManager manager;


    public NotificationHelper(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void sendSimpleNotification(int smallIconRes, String contentTitle, String contentText, PendingIntent contentIntent, int id) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(smallIconRes)
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setContentIntent(contentIntent);
        manager.notify(id, builder.build());
    }

    public void sendSimpleNotification(SimpleCallBack callBack) {
        this.sendSimpleNotification(
                callBack.getSmallIconRes(),
                callBack.getContentTitle(),
                callBack.getContentText(),
                callBack.getPendingIntent(),
                callBack.getId());
    }


    public void sendCustomNotification(CallBack callBack) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), callBack.getLayoutRes());
        callBack.onGetRemoteViews(remoteViews);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(callBack.getSmallIconRes())
                .setContent(remoteViews).build();
        manager.notify(callBack.getId(), notification);
    }


    public interface CallBack {
        int getLayoutRes();

        int getSmallIconRes();

        int getId();

        void onGetRemoteViews(RemoteViews remoteViews);
    }

    public interface SimpleCallBack {
        String getContentTitle();

        String getContentText();

        int getSmallIconRes();

        PendingIntent getPendingIntent();

        int getId();
    }

}
