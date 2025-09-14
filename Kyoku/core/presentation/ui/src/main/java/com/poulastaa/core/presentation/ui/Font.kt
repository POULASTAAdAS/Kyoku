package com.poulastaa.core.presentation.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

private val exo2 = FontFamily(
    Font(
        resId = R.font.exo2_thin,
        weight = FontWeight.Thin
    ),
    Font(
        resId = R.font.exo2_extra_light,
        weight = FontWeight.ExtraLight
    ),
    Font(
        resId = R.font.exo2_light,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.exo2_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.exo2_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.exo2_semi_bold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.exo2_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resId = R.font.exo2_extra_bold,
        weight = FontWeight.Black
    )
)

private val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = exo2),
    displayMedium = baseline.displayMedium.copy(fontFamily = exo2),
    displaySmall = baseline.displaySmall.copy(fontFamily = exo2),

    headlineLarge = baseline.headlineLarge.copy(fontFamily = exo2),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = exo2),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = exo2),

    titleLarge = baseline.titleLarge.copy(fontFamily = exo2),
    titleMedium = baseline.titleMedium.copy(fontFamily = exo2),
    titleSmall = baseline.titleSmall.copy(fontFamily = exo2),

    bodyLarge = baseline.bodyLarge.copy(fontFamily = exo2),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = exo2),
    bodySmall = baseline.bodySmall.copy(fontFamily = exo2),

    labelLarge = baseline.labelLarge.copy(fontFamily = exo2),
    labelMedium = baseline.labelMedium.copy(fontFamily = exo2),
    labelSmall = baseline.labelSmall.copy(fontFamily = exo2),
)