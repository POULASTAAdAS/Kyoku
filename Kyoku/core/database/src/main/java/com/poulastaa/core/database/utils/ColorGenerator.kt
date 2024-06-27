package com.poulastaa.core.database.utils

import android.graphics.Bitmap
import androidx.palette.graphics.Palette

object ColorGenerator {
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

    enum class ColorType {
        VIBRANT,
        DARK_VIBRANT,
        ON_DARK_VIBRANT,
        LIGHT_VIBRANT,
        DOMAIN_SWATCH,
        MUTED_SWATCH,
        LIGHT_MUTED,
        DARK_MUTED
    }
}

private fun parseColorSwatch(color: Palette.Swatch?): String = if (color != null) {
    "#${Integer.toHexString(color.rgb)}"
} else "#000000"

private fun parseBodyColor(color: Int?): String = if (color != null) {
    "#${Integer.toHexString(color)}"
} else "#FFFFFF"

