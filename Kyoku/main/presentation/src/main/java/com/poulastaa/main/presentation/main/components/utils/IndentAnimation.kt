package com.poulastaa.main.presentation.main.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shape

interface IndentAnimation {
    @Composable
    fun animateIndentShapeAsState(
        targetOffset: Offset,
        shapeCornerRadius: ShapeCornerRadius,
    ): State<Shape>
}