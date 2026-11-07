package com.poulastaa.common.ui.image

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import okio.Path

private const val MEMORY_CACHE_PERCENT = 0.25
private const val DISK_CACHE_MAX_SIZE_BYTES = 256L * 1024 * 1024
private const val IMAGE_CACHE_DIR = "image_cache"

/**
 * Directory used by the global image loader's disk cache. Provided per platform.
 */
internal expect fun PlatformContext.imageCacheDir(): Path

/**
 * Builds the app-wide Coil [ImageLoader] shared by every platform.
 */
fun newAppImageLoader(context: PlatformContext): ImageLoader = ImageLoader.Builder(context)
    .components {
        add(KtorNetworkFetcherFactory())
    }
    .memoryCachePolicy(CachePolicy.ENABLED)
    .diskCachePolicy(CachePolicy.ENABLED)
    .memoryCache {
        MemoryCache.Builder()
            .maxSizePercent(context, MEMORY_CACHE_PERCENT)
            .build()
    }
    .diskCache {
        DiskCache.Builder()
            .directory(context.imageCacheDir().resolve(IMAGE_CACHE_DIR))
            .maxSizeBytes(DISK_CACHE_MAX_SIZE_BYTES)
            .build()
    }
    .crossfade(true)
    .build()

/**
 * Registers [newAppImageLoader] as Coil's global singleton. Call once at the root of every
 * platform entry point before any image is requested.
 */
@Composable
fun SetupAppImageLoader() {
    setSingletonImageLoaderFactory { context -> newAppImageLoader(context) }
}
