package com.poulastaa.core.presentation.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
val ballSize = 10.dp
@Stable
val BOTTOM_BAR_HEIGHT = 60.dp

@Stable
fun Dp.toPxf(density: Density): Float = with(density) { this@toPxf.toPx() }

@Stable
@Composable
internal fun Dp.toPxf(): Float = toPxf(LocalDensity.current)

internal fun lerp(start: Float, stop: Float, fraction: Float) =
    (start * (1 - fraction) + stop * fraction)

internal fun chooseCornerSize(sizeHeight: Float, cornerRadius: Float) =
    if (sizeHeight > cornerRadius) cornerRadius
    else sizeHeight