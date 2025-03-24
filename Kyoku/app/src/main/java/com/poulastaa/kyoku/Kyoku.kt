package com.poulastaa.kyoku

import android.app.Application
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

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    fun applicationScope() = applicationScope

    override fun onTerminate() {
        super.onTerminate()
        applicationScope.cancel()
    }

    override val workManagerConfiguration: Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
}
