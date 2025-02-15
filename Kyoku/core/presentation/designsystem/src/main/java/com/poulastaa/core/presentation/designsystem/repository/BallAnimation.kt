package com.poulastaa.core.presentation.designsystem.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import com.poulastaa.core.presentation.designsystem.bottom_bar.BallAnimInfo

interface BallAnimation {
    @Composable
    fun animateAsState(targetOffset: Offset): State<BallAnimInfo>
}