package com.shubhamdev.waterreminder.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkManagerScheduler(private val context: Context) {
    
    fun scheduleReminders(intervalMinutes: Int) {
        val workRequest = PeriodicWorkRequestBuilder<HydrationWorker>(
            intervalMinutes.toLong(),
            TimeUnit.MINUTES,
            15,
            TimeUnit.MINUTES
        ).build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            HydrationWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
    
    fun cancelReminders() {
        WorkManager.getInstance(context).cancelUniqueWork(HydrationWorker.WORK_NAME)
    }
}

