package com.example.company.myapplication

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

class PlayService : Service() {
    var player: MediaPlayer? = null
    var url: String? = null
    var notification: Notifications = Notifications(this)

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "stop") {
            player?.stop()
            notification.manager?.cancel(1)
            stopSelf()
            return START_NOT_STICKY
        }
        notification.getNotification()

        try {
            url = intent?.extras?.getString("mp3")
            player = MediaPlayer()
            player?.setDataSource(this, Uri.parse(url))
            player?.setOnPreparedListener { p ->
                p.start()

                val timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        if (p.isPlaying.not()) {
                            timer.cancel()
                            return
                        }
                        notification.builder?.setContentText("${p.currentPosition / 1000} / ${p.duration / 1000}")
                        notification.manager?.notify(1, notification.build())
                    }
                }, 1000, 1000)
            }
            player?.prepareAsync()

        } catch (e: Exception) {
            Log.e("onStart", e.toString())
        }
        notification.manager?.notify(1, notification.build())

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        player?.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}