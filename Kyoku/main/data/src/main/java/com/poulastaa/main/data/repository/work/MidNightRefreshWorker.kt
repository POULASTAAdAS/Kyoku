package com.poulastaa.main.data.repository.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.poulastaa.main.data.mapper.toWorkResult
import com.poulastaa.main.domain.repository.work.RefreshRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

internal class MidNightRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repo: RefreshRepository,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) return Result.failure()

        return when (val result = repo.refreshSuggestedData()) {
            is com.poulastaa.core.domain.Result.Error -> result.error.toWorkResult()
            is com.poulastaa.core.domain.Result.Success -> Result.success()
        }
    }
}