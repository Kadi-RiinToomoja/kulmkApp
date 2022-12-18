package com.example.kulmkapp.ui

import android.app.PendingIntent
import android.app.PendingIntent.*
import android.app.TaskInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startForegroundService
import com.example.kulmkapp.MainActivity
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.NotificationEntity

//source: https://www.droidcon.com/2022/09/27/everything-you-need-to-know-about-adding-notifications-with-alarm-manager-in-android/
class AlarmReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManagerCompat? = null

    override fun onReceive(p0: Context?, p1: Intent?) {
        val notificationEntity = p1?.getParcelableExtra("task_info") as? NotificationEntity
        // tapResultIntent gets executed when user taps the notification
        val tapResultIntent = Intent(p0, MainActivity::class.java)
        tapResultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent = getActivity( p0,0,tapResultIntent,FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val notification = p0?.let {
            NotificationCompat.Builder(it, "to_do_list")
                .setContentTitle("Expiring Food Reminder")
                .setContentText(notificationEntity?.description)
                .setSmallIcon(R.drawable.dark_fridge)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        }

        notificationManager = p0?.let { NotificationManagerCompat.from(it) }
        notification?.let { notificationEntity?.let { it1 -> notificationManager?.notify(it1.id, it) } }
    }

}