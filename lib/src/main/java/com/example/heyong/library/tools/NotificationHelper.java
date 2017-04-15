package com.example.heyong.library.tools;

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



    @Deprecated
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(callBack.getSmallIconRes())
                .setContentTitle(callBack.getContentTitle())
                .setContentText(callBack.getContentText())
                .setContentIntent(callBack.getPendingIntent());
        callBack.beforeNotificationBuild(builder);
        manager.notify(callBack.getId(), builder.build());
    }

    public void sendCustomNotification(CallBack callBack) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), callBack.getLayoutRes());
        callBack.onGetRemoteViews(remoteViews);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(callBack.getSmallIconRes())
                .setContent(remoteViews);
        callBack.beforeNotificationBuild(builder);
        manager.notify(callBack.getId(), builder.build());
    }

    public NotificationManager getManager() {
        return manager;
    }

    static public abstract class CallBack {
        public abstract int getLayoutRes();

        public abstract int getSmallIconRes();

        public abstract int getId();

        public abstract void onGetRemoteViews(RemoteViews remoteViews);

        public void beforeNotificationBuild(NotificationCompat.Builder builder){}
    }

    static public abstract class SimpleCallBack {
        public abstract String getContentTitle();

        public abstract String getContentText();

        public abstract int getSmallIconRes();

        public abstract PendingIntent getPendingIntent();

        public abstract int getId();

        public void beforeNotificationBuild(NotificationCompat.Builder builder){}
    }

}
