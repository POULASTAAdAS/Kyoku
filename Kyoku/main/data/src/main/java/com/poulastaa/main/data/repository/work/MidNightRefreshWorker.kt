package com.poulastaa.main.data.repository.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.poulastaa.main.domain.repository.work.RefreshRepository
import javax.inject.Inject

internal class MidNightRefreshWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    private val repo: RefreshRepository,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return if (runAttemptCount >= 5) Result.failure()
        else Result.retry()
    }
}