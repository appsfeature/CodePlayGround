package com.example.codeplayground;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

/**
 * Add Permission in Manifest
        <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>

        <service
            android:name=".ForegroundService"
            android:enabled="true"
            android:exported="false"/>
 */

public class ForegroundServiceJava extends Service {
    public static final String CHANNEL_ID = "FSChannel";

    public static void startService(Context context) {
        if(!isRunningServices(context, ForegroundServiceJava.class)) {
            Intent serviceIntent = new Intent(context, ForegroundServiceJava.class);
            serviceIntent.putExtra("inputExtra", "Foreground Service Started");
            ContextCompat.startForegroundService(context, serviceIntent);
            Log.d(ForegroundServiceJava.class.getSimpleName(), "Service started.");
        }else {
            Log.d(ForegroundServiceJava.class.getSimpleName(), "Service already running.");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(intent);
        //do heavy work on a background thread

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startForeground(Intent intent) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSound(null)
                .setSmallIcon(android.R.color.transparent)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            serviceChannel.setSound(null, null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public static boolean isRunningServices(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = manager.getRunningServices(Integer.MAX_VALUE);
        if(serviceList.size() > 0) {
            for (ActivityManager.RunningServiceInfo service : serviceList) {
                if(service.service.getClassName().equalsIgnoreCase(serviceClass.getName())){
                    return true;
                }
            }
        }
        return false;
    }
}
