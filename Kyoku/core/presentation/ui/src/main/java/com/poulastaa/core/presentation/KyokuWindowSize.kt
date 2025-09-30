package com.poulastaa.core.presentation

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable

@Composable
fun KyokuWindowSize(
    windowSizeClass: WindowSizeClass,
    compactContent: @Composable () -> Unit,
    expandedCompactContent: @Composable () -> Unit,
    expandedSmallContent: @Composable () -> Unit,
    mediumContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> compactContent()
        WindowWidthSizeClass.Medium -> mediumContent()
        WindowWidthSizeClass.Expanded -> {
            when (windowSizeClass.heightSizeClass) {
                WindowHeightSizeClass.Compact -> expandedCompactContent()
                WindowHeightSizeClass.Medium -> expandedSmallContent()
                WindowHeightSizeClass.Expanded -> expandedContent()
            }
        }
    }
}