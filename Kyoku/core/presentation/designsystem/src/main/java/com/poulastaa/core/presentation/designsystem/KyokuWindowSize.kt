package com.poulastaa.core.presentation.designsystem

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable

@Composable
fun KyokuWindowSize(
    windowSizeClass: WindowSizeClass,
    compactContent: @Composable () -> Unit,
    mediumContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> compactContent()
        WindowWidthSizeClass.Medium -> mediumContent()
        WindowWidthSizeClass.Expanded -> expandedContent()
    }
}