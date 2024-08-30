package com.poulastaa.kyoku

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Inject

@HiltAndroidApp
class Kyoku : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(workerFactory)
            .build()

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun applicationScope() = applicationScope

    override fun onTerminate() {
        super.onTerminate()
        applicationScope.cancel()
    }
}
