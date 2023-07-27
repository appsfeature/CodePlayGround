package com.example.codeplayground

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


/**
 * Add Permission in Manifest
  <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>

  <service android:name=".ForegroundService"
       android:enabled="true"
       android:exported="false"/>
 */
class ForegroundService : Service() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(intent)
        //do heavy work on a background thread
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun startForeground(intent: Intent) {
        val input = intent.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText(input)
            .setSound(null)
            .setSmallIcon(android.R.color.transparent)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            serviceChannel.setSound(null, null)
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "FSChannel"
        fun startService(context: Context) {
            if (!isRunningServices(context, ForegroundService::class.java)) {
                val serviceIntent = Intent(context, ForegroundService::class.java)
                serviceIntent.putExtra("inputExtra", "Foreground Service Started")
                ContextCompat.startForegroundService(context, serviceIntent)
                Log.d(ForegroundService::class.java.simpleName, "Service started.")
            } else {
                Log.d(ForegroundService::class.java.simpleName, "Service already running.")
            }
        }

        private fun isRunningServices(context: Context, serviceClass: Class<*>): Boolean {
            val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val serviceList = manager.getRunningServices(Int.MAX_VALUE)
            if (serviceList.size > 0) {
                for (service in serviceList) {
                    if (service.service.className.equals(serviceClass.name, ignoreCase = true)) {
                        return true
                    }
                }
            }
            return false
        }
    }
}