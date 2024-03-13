package com.poulastaa.kyoku.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

@Composable
fun AppThem(
    appDimens: Dimens,
    content: @Composable () -> Unit
) {
    val dimens = remember {
        appDimens
    }

    CompositionLocalProvider(value = LocalAppDimens provides dimens) {
        content()
    }
}

val LocalAppDimens = compositionLocalOf {
    CompactDimens
}

@Composable
fun TestThem(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorSchemeTypeTwo else LightColorSchemeTypeTwo

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = AppShape,
        content = content
    )
}
