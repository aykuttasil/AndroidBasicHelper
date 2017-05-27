package com.aykuttasil.androidbasichelperlib;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import java.util.Random;

public class NotificationHelper {

    static NotificationCompat.Builder mBuilder;
    static NotificationManager notificationManager;
    static Notification mNotification;
    Context mContext;
    static int mNotifId;
    static int mIcon = R.drawable.ok;
    static Uri mSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private String TAG = NotificationHelper.class.getSimpleName();

    private NotificationHelper(Context context) {
        mNotifId = new Random().nextInt();
        mContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
    }


    public static NotificationHelper newInstance(Context context) {
        return new NotificationHelper(context);

    }

    public android.support.v4.app.NotificationCompat.Builder getBuilder() {
        return mBuilder;
    }

    public Notification getNotification() {
        return mNotification;
    }

    public static void showSimpleNotification(String title, String text, int icon, int notifId) {

        mNotification = mBuilder
                .setSmallIcon(icon)
                .setTicker(title)
                .setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setSubText(text)
                .setContentText(text)
                .build();

        notificationManager.notify(notifId, mNotification);
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param notifId
     * @param intent
     */
    public static void showNotificationMessage(Context context, String title, String message, int notifId, Intent intent) {


        if (intent == null) {
            intent = new Intent(context, context.getClass());
        }
        int icon = R.drawable.ok;

        int mNotificationId = notifId;
        // AppConfig.NOTIFICATION_ID;


        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT
        );

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        Notification notification = mBuilder
                .setSmallIcon(icon)
                .setTicker(title)
                .setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(inboxStyle)
                .setContentIntent(resultPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon))
                .setSubText(message)
                .setContentText(message)
                .build();

        // notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_SHOW_LIGHTS;
        notification.flags = Notification.FLAG_SHOW_LIGHTS;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationId, notification);
        /*
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;

        if (SuperHelper.isAppIsInBackground(context)) {
            // notification icon
            int icon = R.mipmap.ic_launcher;

            int mNotificationId = 100;
            // AppConfig.NOTIFICATION_ID;

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            intent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            Notification notification = mBuilder
                    .setSmallIcon(icon)
                    .setTicker(title)
                    .setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(inboxStyle)
                    .setContentIntent(resultPendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon))
                    .setContentText(message)
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(mNotificationId, notification);
        } else {
            intent.putExtra("title", title);
            intent.putExtra("message", message);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        }
        */
    }


    public NotificationHelper setNotifTitle(String title) {
        mBuilder.setContentTitle(title);
        return this;
    }

    public NotificationHelper setNotifContent(String content) {
        mBuilder.setContentText(content);
        return this;
    }

    public NotificationHelper setNotifSubContent(String subContent) {
        mBuilder.setSubText(subContent);
        return this;
    }

    public NotificationHelper setNotifId(int id) {
        mNotifId = id;
        return this;
    }

    public NotificationHelper setNotifIcon(int icon) {
        mIcon = icon;
        return this;
    }

    public NotificationHelper setNotifSound(Uri sound) {
        mSound = sound;
        return this;
    }

    public NotificationHelper setNotifContentIntent(PendingIntent pendingIntent) {
        mBuilder.setContentIntent(pendingIntent);
        return this;
    }

    public void notifyNotification() {
        buildNotif();
        showNotif();
    }

    public NotificationHelper getBuildNotification() {
        buildNotif();
        return this;
    }

    public NotificationHelper setNotifNoClear(boolean flag) {
        if (flag) {
            mNotification.flags = NotificationCompat.FLAG_NO_CLEAR;
        }
        return this;
    }

    public NotificationHelper setNotifFlag(int flag) {
        mNotification.flags = flag;
        return this;
    }

    public NotificationHelper buildNotif() {

        Intent intent = new Intent(mContext, mContext.getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, new Random().nextInt(), intent, PendingIntent.FLAG_ONE_SHOT);

        mNotification = mBuilder
                .setSmallIcon(mIcon)
                .setSound(mSound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        return this;
    }

    public void showNotif() {
        notificationManager.notify(mNotifId, mNotification);
    }


}
