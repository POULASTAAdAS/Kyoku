package com.poulastaa.mflix.core.presentation.designsystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

@Stable
fun Dp.toPxf(density: Density): Float = with(density) { this@toPxf.toPx() }

@Stable
@Composable
fun Dp.toPxf(): Float = toPxf(LocalDensity.current)