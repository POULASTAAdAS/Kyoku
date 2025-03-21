package com.poulastaa.main.data.repository.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.poulastaa.main.data.mapper.toWorkResult
import com.poulastaa.main.domain.repository.work.WorkRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncFavouriteSongWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val work: WorkRepository,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) return Result.failure()

        return when (val result = work.syncSavedFavourite()) {
            is com.poulastaa.core.domain.Result.Error -> result.error.toWorkResult()
            is com.poulastaa.core.domain.Result.Success -> Result.success()
        }
    }
}