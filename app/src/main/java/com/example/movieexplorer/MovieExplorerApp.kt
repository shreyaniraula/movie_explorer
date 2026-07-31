package com.example.movieexplorer

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.movieexplorer.core.work.WorkScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

// @HiltAndroidApp sets up dependency injection for the entire app.
@HiltAndroidApp
class MovieExplorerApp : Application(), Configuration.Provider {
    @Inject
    // HiltWorkerFactory is the bridge that tells WorkManager
    // "use Hilt to construct Workers instead of your default reflection-based approach."
    lateinit var workerFactory: HiltWorkerFactory
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    // runs exactly once per process start, the natural place for setup long-lived app infrastructure
    override fun onCreate() {
        super.onCreate()
        WorkScheduler.scheduleCacheCleanup(this)
    }
}