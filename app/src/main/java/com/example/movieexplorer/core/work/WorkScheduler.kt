package com.example.movieexplorer.core.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkScheduler {
    private const val CLEANUP_WORK_NAME = "cache_cleanup_work"

    fun scheduleCacheCleanup(context: Context) {

        // only run when battery isn't low; the OS decides exactly when to fire the job
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        // runs roughly once a day
        val request = PeriodicWorkRequestBuilder<CacheCleanupWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS,
        )
            .setConstraints(constraints)
            .build()

        // only one instance of this job can be scheduled at a time
        // Keep means if this job is already scheduled from a previous app launch
        // dont reschedule it
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            CLEANUP_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}