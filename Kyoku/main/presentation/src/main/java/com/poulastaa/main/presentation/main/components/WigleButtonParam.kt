package com.poulastaa.main.presentation.main.components

import androidx.annotation.FloatRange
import androidx.compose.runtime.Stable

@Stable
data class WiggleButtonParams(
    @FloatRange(from = 0.0, to = 1.0) val scale: Float = 1f,
    @FloatRange(from = 0.0, to = 1.0) val alpha: Float = 1f,
    val radius: Float = 10f,
)