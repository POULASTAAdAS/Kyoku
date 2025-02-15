package com.poulastaa.main.presentation.main.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset

interface BallAnimation {
    @Composable
    fun animateAsState(targetOffset: Offset): State<BallAnimInfo>
}