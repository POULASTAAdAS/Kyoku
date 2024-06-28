package com.poulastaa.core.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

object BitmapConverter {
    fun encodeToSting(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos)
        val byteArray = baos.toByteArray()

        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
    }

    fun decodeToImageBitmap(encodedString: String): ImageBitmap? {
        if (encodedString.startsWith("http")) return null

        return try {
            val encodedBytes =
                android.util.Base64.decode(encodedString, android.util.Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodedBytes, 0, encodedBytes.size).asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
}