package com.poulastaa.core.presentation.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable

@Composable
fun SetupScreenWrapper(
    windowSizeClass: WindowSizeClass,
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    compactContent: @Composable (paddingValue: PaddingValues) -> Unit,
    mediumContent: @Composable (paddingValue: PaddingValues) -> Unit,
    expandedContent: @Composable (paddingValue: PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = topBar,
        floatingActionButton = floatingActionButton
    ) {
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                compactContent(it)
            }

            WindowWidthSizeClass.Medium -> {
                mediumContent(it)
            }

            WindowWidthSizeClass.Expanded -> {
                expandedContent(it)
            }
        }
    }
}