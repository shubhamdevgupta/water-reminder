package com.shubhamdev.waterreminder.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shubhamdev.waterreminder.data.datastore.PreferencesDataSource
import com.shubhamdev.waterreminder.data.datastore.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take

class HydrationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    private val preferencesDataSource = PreferencesDataSource(context.dataStore)
    
    override suspend fun doWork(): Result {
        return try {
            val remindersEnabled = preferencesDataSource.remindersEnabled
                .take(1)
                .first()
            
            if (remindersEnabled) {
                showNotification()
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
    
    private suspend fun showNotification() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // Create notification channel for Android O+
        val channelId = "hydration_reminder_channel"
        val channelName = "Hydration Reminders"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }
        
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Time to Hydrate!")
            .setContentText("Don't forget to drink water and stay hydrated.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(1, notification)
    }
    
    companion object {
        const val WORK_NAME = "HYDRATION_REMINDER"
    }
}


