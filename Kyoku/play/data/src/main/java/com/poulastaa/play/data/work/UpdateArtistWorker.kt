package com.poulastaa.play.data.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.poulastaa.core.domain.repository.work.WorkRepository
import com.poulastaa.play.data.mapper.toWorkResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateArtistWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val work: WorkRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        Log.d("attempt", "UpdateArtistWorker: $runAttemptCount")

        if (runAttemptCount >= 2) return Result.failure()

        return when (val result = work.getUpdatedArtists()) {
            is com.poulastaa.core.domain.utils.Result.Error -> result.error.toWorkResult()

            is com.poulastaa.core.domain.utils.Result.Success -> Result.success()
        }
    }
}