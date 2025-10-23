package com.poulastaa.kyoku

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

@HiltAndroidApp
class Kyoku : Application() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    fun applicationScope() = scope

    override fun onTerminate() {
        super.onTerminate()
        scope.cancel()
    }
}