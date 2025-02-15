package com.poulastaa.core.presentation.designsystem.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shape
import com.poulastaa.core.presentation.designsystem.ui.ShapeCornerRadius

interface IndentAnimation {
    @Composable
    fun animateIndentShapeAsState(
        targetOffset: Offset,
        shapeCornerRadius: ShapeCornerRadius,
    ): State<Shape>
}