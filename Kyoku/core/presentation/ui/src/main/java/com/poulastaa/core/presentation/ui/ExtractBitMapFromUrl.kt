package com.poulastaa.core.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.ImageRequest
import coil.request.SuccessResult
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
fun getBitmapFromUrlOrCache(
    url: String,
    header: String
): State<Bitmap?> {
    val context = LocalContext.current
    val filename = url.hashCode().toString()

    return produceState<Bitmap?>(initialValue = null, url) {
        value = withContext(Dispatchers.IO) {
            loadBitmapFromFile(filename, context) ?: run {
                val loader = ImageLoader.Builder(context)
                    .diskCache {
                        DiskCache.Builder()
                            .directory(context.cacheDir.resolve("image_catch"))
                            .maxSizePercent(0.02)
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
                }
                newBitmap
            }
        }
    }
}


//@Composable
//fun getBitmapFromUrlOrCache(
//    url: String,
//    header: String,
//): Bitmap? {
//    val context = LocalContext.current
//    val filename = url.hashCode().toString()
//    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
//
//    LaunchedEffect(url) {
//        withContext(Dispatchers.IO) {
//            bitmap = loadBitmapFromFile(filename, context)
//
//            if (bitmap == null) {
//                val loader = ImageLoader.Builder(context)
//                    .diskCache {
//                        DiskCache.Builder()
//                            .directory(context.cacheDir.resolve("image_catch"))
//                            .maxSizePercent(0.02)
//                            .build()
//                    }
//                    .build()
//
//                val request = ImageRequest.Builder(context)
//                    .addHeader(
//                        name = if (!header.startsWith("Bearer")) "Cookie" else "Authorization",
//                        value = header
//                    )
//                    .data(url)
//                    .allowHardware(false)
//                    .build()
//
//                val result = (loader.execute(request) as? SuccessResult)?.drawable
//                val newBitmap = (result as? BitmapDrawable)?.bitmap
//
//                newBitmap?.let {
//                    saveBitmapToFile(it, filename, context)
//                    bitmap = it
//                }
//            }
//
//        }
//    }
//
//    return bitmap
//}