package com.poulastaa.core.presentation.designsystem.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.poulastaa.core.presentation.designsystem.R

private val Dosis = FontFamily(
    Font(
        resId = R.font.dosis_extra_light,
        weight = FontWeight.ExtraLight
    ),
    Font(
        resId = R.font.dosis_light,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.dosis_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.dosis_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.dosis_semi_bold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.dosis_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resId = R.font.dosis_extra_bold,
        weight = FontWeight.Black
    )
)

private val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = Dosis),
    displayMedium = baseline.displayMedium.copy(fontFamily = Dosis),
    displaySmall = baseline.displaySmall.copy(fontFamily = Dosis),

    headlineLarge = baseline.headlineLarge.copy(fontFamily = Dosis),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = Dosis),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = Dosis),

    titleLarge = baseline.titleLarge.copy(fontFamily = Dosis),
    titleMedium = baseline.titleMedium.copy(fontFamily = Dosis),
    titleSmall = baseline.titleSmall.copy(fontFamily = Dosis),

    bodyLarge = baseline.bodyLarge.copy(fontFamily = Dosis),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = Dosis),
    bodySmall = baseline.bodySmall.copy(fontFamily = Dosis),

    labelLarge = baseline.labelLarge.copy(fontFamily = Dosis),
    labelMedium = baseline.labelMedium.copy(fontFamily = Dosis),
    labelSmall = baseline.labelSmall.copy(fontFamily = Dosis),
)