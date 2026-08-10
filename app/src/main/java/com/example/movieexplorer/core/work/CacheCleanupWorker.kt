package com.example.movieexplorer.core.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.movieexplorer.domain.repository.MovieRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

// WorkManager survives things a plain coroutine can't

// @HiltWorker tells HIltWorkerFactory - this Worker needs special handling
// build the Hilt-known parts, and accept the WorkManager-known parts separately.
@HiltWorker
// AssistedInject replaces the usual Inject on the constructor, since this constructor mixes two sources of parameters instead of one.
class CacheCleanupWorker @AssistedInject constructor(
    // @Assisted marks which parameters come from the caller, not from Hilt.
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: MovieRepository,
) : CoroutineWorker(context, params) {
    // When a class needs some dependencies from Hilt's graph,
    // but also needs one or more values only available at the exact moment of creation
    // like Worker's runtime Context/WorkerParameters,
    // or a ViewModel needing a runtime-only parameter alongside injected ones.
    // Regular @Inject constructor can't mix "graph-provided" and "caller-provided" parameters;
    // @AssistedInject/@Assisted explicitly separates the two.
    override suspend fun doWork(): Result {
        return try {
            repository.cleanupOldRecentlyViewed(olderThanDays = 30)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}