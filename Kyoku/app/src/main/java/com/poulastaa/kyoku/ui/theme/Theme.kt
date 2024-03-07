package com.poulastaa.kyoku.ui.theme

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun KyokuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    activity: Activity = LocalContext.current as Activity,
    darkColor: ColorScheme,
    lightColor: ColorScheme,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColor
        else -> lightColor
    }
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

    val window = calculateWindowSizeClass(activity = activity)
    val config = LocalConfiguration.current

    var typography = CompactTypography
    var appDimens = CompactDimens

    when (window.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            if (config.screenWidthDp <= 360) {
                appDimens = CompactSmallDimens
                typography = CompactSmallTypography
                Log.d(
                    "called",
                    "Compact 360 : ${config.screenWidthDp} , ${config.screenHeightDp}"
                )
            } else if (config.screenWidthDp < 599) {
                appDimens = CompactMediumDimens
                typography = CompactMediumTypography
                Log.d(
                    "called",
                    "Compact < 599 : ${config.screenWidthDp} , ${config.screenHeightDp}"
                )
            } else {
                appDimens = CompactDimens
                typography = CompactTypography
                Log.d(
                    "called",
                    "Compact > 599 : ${config.screenWidthDp} , ${config.screenHeightDp}"
                )
            }
        }

        WindowWidthSizeClass.Medium -> {
            appDimens = MediumDimens
            typography = MediumTypography
            Log.d("called", "Medium : ${config.screenWidthDp} , ${config.screenHeightDp}")
        }

        else -> {
            appDimens = ExpandedDimens
            typography = ExpandedTypography
            Log.d("called", "Expanded : ${config.screenWidthDp} , ${config.screenHeightDp}")
        }
    }

    AppThem(appDimens = appDimens) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}

val MaterialTheme.dimens
    @Composable
    get() = LocalAppDimens.current
