package com.poulastaa.mflix.core.presentation.designsystem.utils

fun lerp(start: Float, stop: Float, fraction: Float) =
    (start * (1 - fraction) + stop * fraction)