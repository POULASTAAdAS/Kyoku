package com.poulastaa.kyoku.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


@Composable
fun KyokuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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

            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = colorScheme.primary.toArgb()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }

            val windowsInsetsController =
                WindowCompat.getInsetsController(window, view)

            windowsInsetsController.isAppearanceLightStatusBars = darkTheme
            windowsInsetsController.isAppearanceLightNavigationBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}