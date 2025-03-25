package com.poulastaa.main.domain.repository.work

import kotlin.time.Duration

interface SyncLibraryScheduler {
    fun scheduleSync(interval: Duration)
    fun cancelAllSyncs()
}