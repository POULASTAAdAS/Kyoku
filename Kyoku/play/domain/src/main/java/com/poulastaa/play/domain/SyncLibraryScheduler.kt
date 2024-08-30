package com.poulastaa.play.domain

import kotlin.time.Duration

interface SyncLibraryScheduler {
    suspend fun scheduleSync(interval: Duration)
    suspend fun cancelAllSyncs()
}