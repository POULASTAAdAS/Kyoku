package com.poulastaa.common.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.poulastaa.common.ui.design_system.calculateWindowSizeClass

enum class ScreenSizeType {
    CompactVertical,
    LargeVertical,
    CompactHorizontal,
    LargeHorizontal,
}

@Composable
fun ScreenSizeWrapper(
    modifier: Modifier = Modifier.fillMaxSize(),
    windowSizeClass: WindowSizeClass = calculateWindowSizeClass(),
    content: @Composable (ScreenSizeType) -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        content(
            windowSizeClass.toScreenSizeType(
                isHorizontal = maxWidth > maxHeight,
            )
        )
    }
}

fun WindowSizeClass.toScreenSizeType(
    isHorizontal: Boolean,
): ScreenSizeType {
    val isLarge = widthSizeClass != WindowWidthSizeClass.Compact &&
        heightSizeClass != WindowHeightSizeClass.Compact

    return when {
        isHorizontal && isLarge -> ScreenSizeType.LargeHorizontal
        isHorizontal -> ScreenSizeType.CompactHorizontal
        isLarge -> ScreenSizeType.LargeVertical
        else -> ScreenSizeType.CompactVertical
    }
}
