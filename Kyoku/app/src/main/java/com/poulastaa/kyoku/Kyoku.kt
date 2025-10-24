package com.poulastaa.kyoku

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.poulastaa.core.data.repsitory.KyokuImageLoader
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Inject

@HiltAndroidApp
class Kyoku : Application(), SingletonImageLoader.Factory {
    @Inject
    lateinit var imageFactory: KyokuImageLoader

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    fun applicationScope() = scope

    override fun onTerminate() {
        super.onTerminate()
        scope.cancel()
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader = imageFactory.init(context)
}