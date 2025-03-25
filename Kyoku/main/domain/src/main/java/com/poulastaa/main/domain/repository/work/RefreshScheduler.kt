package com.poulastaa.main.domain.repository.work

interface RefreshScheduler {
    fun scheduleRefresh()
    fun cancelRefresh()
}