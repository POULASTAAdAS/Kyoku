package com.poulastaa.core.presentation.designsystem

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.ImageRequest
import com.poulastaa.core.domain.repository.DatastoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class CacheImageReq @Inject constructor() {
    companion object REQUEST {
        @Volatile
        private var header: String = ""

        internal fun init(ds: DatastoreRepository) {
            CoroutineScope(Dispatchers.IO).launch {
                ds.readTokenOrCookie().collectLatest {
                    header = it
                }
            }
        }

        fun imageReq(
            url: String?,
            context: Context,
        ): ImageRequest {
            ImageLoader.Builder(context)
                .diskCache {
                    DiskCache.Builder()
                        .directory(context.cacheDir.resolve("image_catch"))
                        .maxSizePercent(0.04)
                        .build()
                }
                .build()

            return ImageRequest.Builder(context)
                .addHeader(
                    name = if (!header.startsWith("Bearer")) "Cookie" else "Authorization",
                    value = header
                )
                .data(url)
                .build()
        }
    }
}
