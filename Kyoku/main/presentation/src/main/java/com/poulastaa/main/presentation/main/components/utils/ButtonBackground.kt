package com.poulastaa.main.presentation.main.components.utils

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.DpOffset

data class ButtonBackground(
    @DrawableRes val icon: Int,
    val offset: DpOffset = DpOffset.Companion.Zero,
)
