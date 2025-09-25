package com.poulastaa.core.presentation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
fun KyokuWindowSize(
    windowSizeClass: WindowSizeClass,
    compactContent: @Composable () -> Unit,
    expandedCompactContent: @Composable () -> Unit,
    mediumContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
) {
    val info = LocalWindowInfo.current

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> compactContent()
        WindowWidthSizeClass.Medium -> mediumContent()
        WindowWidthSizeClass.Expanded -> if (info.containerSize.width > 980) expandedContent()
        else expandedCompactContent()
    }
}