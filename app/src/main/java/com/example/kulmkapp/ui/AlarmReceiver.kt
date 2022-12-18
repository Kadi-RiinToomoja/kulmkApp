package com.example.kulmkapp.ui

import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.kulmkapp.MainActivity
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.NotificationEntity

//source: https://www.droidcon.com/2022/09/27/everything-you-need-to-know-about-adding-notifications-with-alarm-manager-in-android/
class AlarmReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManagerCompat? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationEntity = intent?.getParcelableExtra("task_info") as? NotificationEntity
        // tapResultIntent gets executed when user taps the notification
        val tapResultIntent = Intent(context, MainActivity::class.java)
        tapResultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent = getActivity( context,0,tapResultIntent,FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val notification = context?.let {
            NotificationCompat.Builder(it, "to_do_list")
                .setContentTitle(context.resources.getString(R.string.notification_title))
                .setContentText(notificationEntity?.description)
                .setSmallIcon(R.drawable.dark_fridge)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        }

        notificationManager = context?.let { NotificationManagerCompat.from(it) }
        notification?.let { notificationEntity?.let { it1 -> notificationManager?.notify(it1.id, it) } }
    }

}