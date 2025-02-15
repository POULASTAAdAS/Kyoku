package com.poulastaa.core.presentation.designsystem.ui

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    secondary = secondaryLight,
    tertiary = tertiaryLight,
    primaryContainer = containerLight,
    error = errorLight,
    errorContainer = errorContainerLight,
    background = backgroundLight,
    surface = backgroundLight
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    secondary = secondaryDark,
    tertiary = tertiaryDark,
    primaryContainer = containerDark,
    error = errorDark,
    errorContainer = errorContainerDark,
    background = backgroundDark,
    surface = backgroundDark,
)

private val LocalAppDimens = compositionLocalOf {
    CompactDimens
}

val MaterialTheme.dimens
    @Composable
    get() = LocalAppDimens.current

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun KyokuThem(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val activity = LocalContext.current
    val view = LocalView.current

    if (!view.isInEditMode) SideEffect {
        val window = (view.context as Activity).window

        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            window.isNavigationBarContrastEnforced = false

        val windowsInsetsController =
            WindowCompat.getInsetsController(window, view)

        windowsInsetsController.isAppearanceLightStatusBars = !darkTheme
        windowsInsetsController.isAppearanceLightNavigationBars = !darkTheme
    }

    val window = calculateWindowSizeClass(activity = activity as Activity)
    val config = LocalConfiguration.current

    val appDimens: Dimens = when (window.widthSizeClass) {
        WindowWidthSizeClass.Compact -> if (config.screenWidthDp <= 360) CompactSmallDimens
        else if (config.screenWidthDp < 599) CompactMediumDimens
        else CompactDimens

        WindowWidthSizeClass.Medium -> MediumDimens

        else -> ExpandedDimens
    }

    AppThem(
        isDarkTheme = darkTheme,
        appDimens = appDimens,
        content = content
    )
}

@Composable
fun AppThem(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    appDimens: Dimens = CompactMediumDimens,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        isDarkTheme -> darkScheme
        else -> lightScheme
    }
    val dimens = remember { appDimens }

    CompositionLocalProvider(value = LocalAppDimens provides dimens) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShape,
            content = content
        )
    }
}