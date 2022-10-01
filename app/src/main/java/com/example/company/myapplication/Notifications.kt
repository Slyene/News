package com.example.company.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

class Notifications(context: Context) {
    var builder: NotificationCompat.Builder? = null
    var manager: NotificationManager? = null
    var channel: NotificationChannel? = null
    val context = context

    fun getNotification() {
        val notificationIntent = Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val iStop = Intent(context, PlayService::class.java).setAction("stop")
        val piStop = PendingIntent.getService(context, 0, iStop, PendingIntent.FLAG_IMMUTABLE)

        builder = NotificationCompat.Builder(context, "1")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("MP3")
            .setContentText("")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE))
            .addAction(R.mipmap.ic_launcher, "Stop", piStop)
            .setAutoCancel(true)
            .setOngoing(false)

        manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel("1", "name", NotificationManager.IMPORTANCE_LOW)
            manager?.createNotificationChannel(channel!!)
        }
    }

    fun build (): Notification {
        return builder!!.build()
    }
}