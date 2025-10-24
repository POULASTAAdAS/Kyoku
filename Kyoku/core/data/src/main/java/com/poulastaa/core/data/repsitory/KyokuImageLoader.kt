package com.poulastaa.core.data.repsitory

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.intercept.Interceptor
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.poulastaa.core.data.BuildConfig
import javax.inject.Inject
import javax.inject.Named

class KyokuImageLoader @Inject constructor(
    @param:Named("ImageAuthHeaderInterceptor")
    private val authInterCeptor: Interceptor,
) {
    fun init(context: PlatformContext) = ImageLoader.Builder(context)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context, 0.25)
                .strongReferencesEnabled(true)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("cover_cache"))
                .maxSizePercent(0.05)
                .build()
        }
        .components { add(authInterCeptor) }
        .crossfade(true)
        .apply { if (BuildConfig.DEBUG) logger(DebugLogger()) }
        .build()
}