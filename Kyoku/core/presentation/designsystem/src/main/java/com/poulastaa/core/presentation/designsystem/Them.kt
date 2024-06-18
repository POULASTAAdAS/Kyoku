package com.poulastaa.core.presentation.designsystem

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
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
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    inversePrimary = inversePrimaryLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark
)


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun KyokuThem(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme = when {
        darkTheme -> darkScheme
        else -> lightScheme
    },
    content: @Composable () -> Unit,
) {
    val activity = LocalContext.current

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }

            val windowsInsetsController =
                WindowCompat.getInsetsController(window, view)

            windowsInsetsController.isAppearanceLightStatusBars = !darkTheme
            windowsInsetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    val window = calculateWindowSizeClass(activity = activity as Activity)
    val config = LocalConfiguration.current

    val appDimens: Dimens
    when (window.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            appDimens = if (config.screenWidthDp <= 360) {
                CompactSmallDimens
            } else if (config.screenWidthDp < 599) {
                CompactMediumDimens
            } else {
                CompactDimens
            }
        }

        WindowWidthSizeClass.Medium -> {
            appDimens = MediumDimens
        }

        else -> {
            appDimens = ExpandedDimens
        }
    }

    AppThem(
        isDarkTheme = darkTheme,
        colorScheme = colorScheme,
        appDimens = appDimens,
        content = content
    )
}

private val LocalAppDimens = compositionLocalOf {
    CompactDimens
}

val MaterialTheme.dimens
    @Composable
    get() = LocalAppDimens.current

@Composable
fun AppThem(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme = when {
        isDarkTheme -> darkScheme
        else -> lightScheme
    },
    appDimens: Dimens = CompactMediumDimens,
    content: @Composable () -> Unit,
) {
    val dimens = remember {
        appDimens
    }

    CompositionLocalProvider(value = LocalAppDimens provides dimens) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShape,
            content = content
        )
    }
}