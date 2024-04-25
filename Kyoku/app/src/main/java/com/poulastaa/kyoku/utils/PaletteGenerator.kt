package com.poulastaa.kyoku.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.poulastaa.kyoku.R


object PaletteGenerator {
    suspend fun convertImageUrlToBitMap(
        isDarkThem: Boolean,
        url: String,
        isCookie: Boolean,
        header: String,
        context: Context
    ): Bitmap {
        val loader = ImageLoader(context)
        val req = ImageRequest.Builder(context)
            .data(url)
            .addHeader(
                name = if (isCookie) "Cookie" else "Authorization",
                value = header
            ).build()

        val imageReq = loader.execute(req)

        return if (imageReq is SuccessResult) {
            (imageReq.drawable as BitmapDrawable).bitmap
        } else {
            BitmapFactory.decodeResource(
                context.resources,
                if (isDarkThem) R.drawable.night_logo else R.drawable.light_logo
            )
        }
    }

    fun extractColorFromBitMap(bitmap: Bitmap): Map<ColorType, String> {
        return mapOf(
            ColorType.VIBRANT to parseColorSwatch(
                color = Palette.from(bitmap).generate().vibrantSwatch
            ),
            ColorType.DARK_VIBRANT to parseColorSwatch(
                color = Palette.from(bitmap).generate().darkVibrantSwatch
            ),
            ColorType.ON_DARK_VIBRANT to parseBodyColor(
                color = Palette.from(bitmap).generate().darkVibrantSwatch?.bodyTextColor
            ),
            ColorType.LIGHT_VIBRANT to parseColorSwatch(
                color = Palette.from(bitmap).generate().lightVibrantSwatch
            ),
            ColorType.DOMAIN_SWATCH to parseColorSwatch(
                color = Palette.from(bitmap).generate().dominantSwatch
            ),
            ColorType.MUTED_SWATCH to parseColorSwatch(
                color = Palette.from(bitmap).generate().mutedSwatch
            ),
            ColorType.LIGHT_MUTED to parseColorSwatch(
                color = Palette.from(bitmap).generate().lightMutedSwatch
            ),
            ColorType.DARK_MUTED to parseColorSwatch(
                color = Palette.from(bitmap).generate().darkMutedSwatch
            )
        )
    }

    private fun parseColorSwatch(color: Palette.Swatch?): String = if (color != null) {
        "#${Integer.toHexString(color.rgb)}"
    } else "#000000"

    private fun parseBodyColor(color: Int?): String = if (color != null) {
        "#${Integer.toHexString(color)}"
    } else "#FFFFFF"
}