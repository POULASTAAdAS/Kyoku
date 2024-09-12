package com.poulastaa.core.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.poulastaa.core.presentation.designsystem.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

private fun saveBitmapToFile(bitmap: Bitmap, filename: String, context: Context): File {
    val file = File(context.cacheDir, filename)
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    return file
}

private fun loadBitmapFromFile(filename: String, context: Context): Bitmap? {
    val file = File(context.cacheDir, filename)
    return if (file.exists()) {
        BitmapFactory.decodeFile(file.absolutePath)
    } else null
}

@Composable
fun imageReq(
    header: String,
    url: String,
): ImageRequest {
    val context = LocalContext.current

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

@Composable
fun imageReqSongCover(
    header: String,
    url: String,
): ImageRequest {
    val context = LocalContext.current

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
        .placeholder(R.drawable.ic_music_vector)
        .error(R.drawable.ic_music_vector)
        .data(url)
        .build()
}

@Composable
fun imageReqUser(
    header: String,
    url: String,
): ImageRequest {
    val context = LocalContext.current

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
        .placeholder(R.drawable.ic_user)
        .error(R.drawable.ic_user)
        .data(url)
        .build()
}

suspend fun getBitmapFromUrlOrCache(
    url: String,
    header: String,
    context: Context,
): Bitmap? {
    val filename = url.hashCode().toString()
    var bitmap: Bitmap?

    withContext(Dispatchers.IO) {
        bitmap = loadBitmapFromFile(filename, context)

        if (bitmap == null) {
            val loader = ImageLoader.Builder(context)
                .diskCache {
                    DiskCache.Builder()
                        .directory(context.cacheDir.resolve("image_catch"))
                        .maxSizePercent(0.04)
                        .build()
                }
                .build()

            val request = ImageRequest.Builder(context)
                .addHeader(
                    name = if (!header.startsWith("Bearer")) "Cookie" else "Authorization",
                    value = header
                )
                .data(url)
                .allowHardware(false)
                .build()

            val result = (loader.execute(request) as? SuccessResult)?.drawable
            val newBitmap = (result as? BitmapDrawable)?.bitmap

            newBitmap?.let {
                saveBitmapToFile(it, filename, context)
                bitmap = it
            }
        }

    }

    return bitmap
}