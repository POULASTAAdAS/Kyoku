package com.poulastaa.core.presentation.designsystem.model

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class CircularReveal(
    val backgroundColor: Color,
    val revealSize: Animatable<Float, AnimationVector1D>,
    val center: Offset,
)