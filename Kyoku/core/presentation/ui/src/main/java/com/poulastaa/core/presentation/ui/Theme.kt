package com.poulastaa.core.presentation.ui

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.poulastaa.core.domain.ThemColor

private val baseLightScheme = lightColorScheme(
    primary = light_primary,
    secondary = light_secondary,
    tertiary = light_tertiary,
    background = light_background,
    primaryContainer = light_container,
    secondaryContainer = light_neutral,
    surface = light_white,
    error = light_error,
    errorContainer = light_error_container
)

private val baseDarkScheme = darkColorScheme(
    primary = dark_primary,
    secondary = dark_secondary,
    tertiary = dark_tertiary,
    background = dark_background,
    primaryContainer = dark_container,
    secondaryContainer = dark_neutral,
    surface = dark_white,
    error = dark_error,
    errorContainer = dark_error_container
)

private val LocalAppDimens = staticCompositionLocalOf {
    CompactDimens
}

val MaterialTheme.dimens
    @ReadOnlyComposable
    @Composable
    get() = LocalAppDimens.current

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun KyokuThem(
    mode: Boolean,
    themColor: ThemColor,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val window = calculateWindowSizeClass(activity = context as Activity)
    val config = LocalConfiguration.current

    val appDimens = when (window.widthSizeClass) {
        WindowWidthSizeClass.Compact -> if (config.screenWidthDp <= 360) CompactSmallDimens
        else if (config.screenWidthDp < 599) CompactMediumDimens
        else CompactDimens

        WindowWidthSizeClass.Medium -> MediumDimens
        else -> ExpandedDimens
    }

    AppTheme(
        mode = mode,
        themColor = themColor,
        appDimens = appDimens,
        content = content
    )
}

@Composable
fun AppTheme(
    mode: Boolean = true,
    themColor: ThemColor = ThemColor.BASE,
    appDimens: Dimens = CompactMediumDimens,
    content: @Composable () -> Unit,
) {
    val colorScheme = when (themColor) {
        ThemColor.BASE -> if (mode) baseDarkScheme else baseLightScheme
        ThemColor.GREEN -> TODO()
    }

    CompositionLocalProvider(value = LocalAppDimens provides appDimens) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShape,
            content = content
        )
    }
}