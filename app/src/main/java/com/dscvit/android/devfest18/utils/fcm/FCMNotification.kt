package com.dscvit.android.devfest18.utils.fcm

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

import androidx.core.app.NotificationCompat
import com.dscvit.android.devfest18.ui.MainActivity

import com.dscvit.android.devfest18.R

/**
 * Helper class for showing and canceling fcm
 * notifications.
 *
 *
 * This class makes heavy use of the [NotificationCompat.Builder] helper
 * class to create notifications in a backward-compatible way.
 */
object FCMNotification {

    private val NOTIFICATION_TAG = "FCM"
    private val FCM_CHANNEL_ID = "com.dscvit.android.devfest18.FCM_CHANNEL_ID"

    fun notify(context: Context,
               exampleString: String) {
        val res = context.resources

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.
//        val picture = BitmapFactory.decodeResource(res, R.drawable.example_picture)


        val title = res.getString(
                R.string.fcm_notification_title_template, exampleString)
        val text = res.getString(
                R.string.fcm_notification_placeholder_text_template, exampleString)

        val builder = NotificationCompat.Builder(context)

                .setDefaults(Notification.DEFAULT_ALL)

//                .setSmallIcon(R.drawable.ic_stat_fcm)
                .setContentTitle(title)
                .setContentText(text)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                .setChannelId(FCM_CHANNEL_ID)

                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                Intent(context, MainActivity::class.java),
                                PendingIntent.FLAG_UPDATE_CURRENT))

                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(text)
                        .setBigContentTitle(title)
                        .setSummaryText("Dummy summary text"))

//                .addAction(
//                        R.drawable.ic_action_stat_share,
//                        res.getString(R.string.action_share),
//                        PendingIntent.getActivity(
//                                context,
//                                0,
//                                Intent.createChooser(Intent(Intent.ACTION_SEND)
//                                        .setType("text/plain")
//                                        .putExtra(Intent.EXTRA_TEXT, "Dummy text"), "Dummy title"),
//                                PendingIntent.FLAG_UPDATE_CURRENT))
//                .addAction(
//                        R.drawable.ic_action_stat_reply,
//                        res.getString(R.string.action_reply),
//                        null)

                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true)

        notify(context, builder.build())
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private fun notify(context: Context, notification: Notification) {
        val nm = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannnel(nm)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification)
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification)
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    fun cancel(context: Context) {
        val nm = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0)
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode())
        }
    }

    private fun createChannnel(nm: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Messages"
            val description = "Notifies when we wan't to send you some messages"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(FCM_CHANNEL_ID, name, importance)
            channel.description = description
            nm.createNotificationChannel(channel)
        }
    }
}
