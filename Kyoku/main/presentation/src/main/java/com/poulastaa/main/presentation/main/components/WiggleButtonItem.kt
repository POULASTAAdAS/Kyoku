package com.poulastaa.main.presentation.main.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
data class WiggleButtonItem(
    @DrawableRes val backgroundIcon: Int,
    @DrawableRes val icon: Int,
    var isSelected: Boolean,
    @StringRes val description: Int,
)