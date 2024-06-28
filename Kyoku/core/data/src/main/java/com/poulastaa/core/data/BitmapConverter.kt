package com.poulastaa.core.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory

object BitmapConverter {
    fun decodeToBitmap(encodedString: String): Bitmap? {
        return try {
            val encodedBytes =
                android.util.Base64.decode(encodedString, android.util.Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodedBytes, 0, encodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }
}