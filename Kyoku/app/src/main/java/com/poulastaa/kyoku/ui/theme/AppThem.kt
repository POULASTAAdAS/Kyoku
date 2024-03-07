package com.poulastaa.kyoku.ui.theme

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
