package com.aykuttasil.androidbasichelperlib

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class NotificationHelper private constructor(private var mContext: Context) {
    private val TAG = NotificationHelper::class.java.simpleName

    val builder: NotificationCompat.Builder
        get() = mBuilder

    val notification: Notification
        get() = mNotification

    val buildNotification: NotificationHelper
        get() {
            buildNotif()
            return this
        }

    init {
        mNotifId = Random.nextInt()
        notificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mBuilder = NotificationCompat.Builder(mContext)
    }


    fun setNotifTitle(title: String): NotificationHelper {
        mBuilder.setContentTitle(title)
        return this
    }

    fun setNotifContent(content: String): NotificationHelper {
        mBuilder.setContentText(content)
        return this
    }

    fun setNotifSubContent(subContent: String): NotificationHelper {
        mBuilder.setSubText(subContent)
        return this
    }

    fun setNotifId(id: Int): NotificationHelper {
        mNotifId = id
        return this
    }

    fun setNotifIcon(icon: Int): NotificationHelper {
        mIcon = icon
        return this
    }

    fun setNotifSound(sound: Uri): NotificationHelper {
        mSound = sound
        return this
    }

    fun setNotifContentIntent(pendingIntent: PendingIntent): NotificationHelper {
        mBuilder.setContentIntent(pendingIntent)
        return this
    }

    fun notifyNotification() {
        buildNotif()
        showNotif()
    }

    fun setNotifNoClear(flag: Boolean): NotificationHelper {
        if (flag) {
            mNotification.flags = NotificationCompat.FLAG_NO_CLEAR
        }
        return this
    }

    fun setNotifFlag(flag: Int): NotificationHelper {
        mNotification.flags = flag
        return this
    }

    fun buildNotif(): NotificationHelper {
        val intent = Intent(mContext, mContext.javaClass)
        val pendingIntent = PendingIntent.getActivity(mContext, Random.nextInt(), intent, PendingIntent.FLAG_ONE_SHOT)

        mNotification = mBuilder
            .setSmallIcon(mIcon)
            .setSound(mSound)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        return this
    }

    fun showNotif() {
        notificationManager.notify(mNotifId, mNotification)
    }

    companion object {
        lateinit var mBuilder: NotificationCompat.Builder
        lateinit var notificationManager: NotificationManager
        lateinit var mNotification: Notification
        internal var mNotifId: Int = 0
        internal var mIcon = R.drawable.ok
        internal var mSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


        fun newInstance(context: Context): NotificationHelper {
            return NotificationHelper(context)
        }

        fun showSimpleNotification(title: String, text: String, icon: Int, notifId: Int) {
            mNotification = mBuilder
                .setSmallIcon(icon)
                .setTicker(title)
                .setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setSubText(text)
                .setContentText(text)
                .build()

            notificationManager.notify(notifId, mNotification)
        }

        fun showNotificationMessage(context: Context, title: String, message: String, notifId: Int, intent: Intent?) {
            var _intent = intent

            if (_intent == null) {
                _intent = Intent(context, context.javaClass)
            }
            val icon = R.drawable.ok

            val mNotificationId = notifId
            // AppConfig.NOTIFICATION_ID;


            val resultPendingIntent = PendingIntent.getActivity(context,
                0,
                _intent,
                PendingIntent.FLAG_ONE_SHOT
            )

            val inboxStyle = NotificationCompat.InboxStyle()

            val mBuilder = NotificationCompat.Builder(context)

            val notification = mBuilder
                .setSmallIcon(icon)
                .setTicker(title)
                .setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(inboxStyle)
                .setContentIntent(resultPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, icon))
                .setSubText(message)
                .setContentText(message)
                .build()

            // notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_SHOW_LIGHTS;
            notification.flags = Notification.FLAG_SHOW_LIGHTS

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(mNotificationId, notification)
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
    }


}
