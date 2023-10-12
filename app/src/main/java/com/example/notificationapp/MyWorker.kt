package com.example.notificationapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context:Context, workerParameters: WorkerParameters): Worker(context,workerParameters) {
    companion object{
        const val CHANNEL_ID = "channelId"
        const val CHANNEL_NAME = "channelName"
        const val NOTIFICATION_ID=1
    }
    override fun doWork(): Result {
        Log.d("MyWorker", "doWork: success ")
        showNotification()
        return Result.success()
    }
    fun showNotification(){
        val intent = Intent(applicationContext,MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_MUTABLE)
        }

        val notification = NotificationCompat.Builder(applicationContext,CHANNEL_ID)
            .setContentTitle("Notification Test")
            .setContentText("This is text of notification")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel);
        }

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(NOTIFICATION_ID,notification)
    }

}