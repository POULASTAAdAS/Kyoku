package com.poulastaa.main.presentation.main.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.poulastaa.mflix.core.presentation.designsystem.utils.toPxf

data class ShapeCornerRadius(
    val topLeft: Float,
    val topRight: Float,
    val bottomRight: Float,
    val bottomLeft: Float,
)

fun shapeCornerRadius(cornerRadius: Float) = ShapeCornerRadius(
    topLeft = cornerRadius,
    topRight = cornerRadius,
    bottomRight = cornerRadius,
    bottomLeft = cornerRadius
)

@Composable
fun shapeCornerRadius(cornerRadius: Dp) = ShapeCornerRadius(
    topLeft = cornerRadius.toPxf(),
    topRight = cornerRadius.toPxf(),
    bottomRight = cornerRadius.toPxf(),
    bottomLeft = cornerRadius.toPxf()
)