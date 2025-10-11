package com.poulastaa.core.presentation.ui

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.domain.ThemColor
import com.poulastaa.core.presentation.ThemeManager

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

private val LocalAppDimens = compositionLocalOf {
    CompactDimens
}

val MaterialTheme.dimens
    @ReadOnlyComposable
    @Composable
    get() = LocalAppDimens.current

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun KyokuThem(
    content: @Composable () -> Unit,
) {
    val mode by ThemeManager.instance.isModeDark.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val view = LocalView.current

    DisposableEffect(mode) {
        activity.enableEdgeToEdge(
            statusBarStyle = if (mode) SystemBarStyle.light(
                scrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb()
            ) else SystemBarStyle.dark(
                scrim = Color.Transparent.toArgb()
            ),
            navigationBarStyle = if (mode) SystemBarStyle.light(
                scrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb()
            ) else SystemBarStyle.dark(
                scrim = Color.Transparent.toArgb()
            )
        )

        onDispose { }
    }

    if (!view.isInEditMode) SideEffect {
        val window = (view.context as Activity).window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            window.isNavigationBarContrastEnforced = false

        val windowsInsetsController = WindowCompat.getInsetsController(window, view)

        windowsInsetsController.isAppearanceLightStatusBars = !mode
        windowsInsetsController.isAppearanceLightNavigationBars = !mode
    }

    val window = calculateWindowSizeClass(activity = context as Activity)
    val config = LocalConfiguration.current

    val appDimens: Dimens = when (window.widthSizeClass) {
        WindowWidthSizeClass.Compact -> if (config.screenWidthDp <= 360) CompactSmallDimens
        else if (config.screenWidthDp < 599) CompactMediumDimens
        else CompactDimens

        WindowWidthSizeClass.Medium -> MediumDimens

        else -> ExpandedDimens
    }


    AppTheme(
        mode = mode,
        themColor = ThemeManager.instance.themColor,
        appDimens = appDimens,
        content = content
    )
}

@Composable
fun AppTheme(
    them: Boolean? = null,
    mode: Boolean = true,
    themColor: ThemColor = ThemColor.BASE,
    appDimens: Dimens = CompactMediumDimens,
    content: @Composable () -> Unit,
) {
    val colorScheme = when (themColor) {
        ThemColor.BASE -> if (them ?: mode) baseDarkScheme else baseLightScheme
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