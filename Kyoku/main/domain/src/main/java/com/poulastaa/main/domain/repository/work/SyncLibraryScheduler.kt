package com.poulastaa.main.domain.repository.work

import kotlin.time.Duration

interface SyncLibraryScheduler {
    suspend fun scheduleSync(interval: Duration)
    suspend fun cancelAllSyncs()
}