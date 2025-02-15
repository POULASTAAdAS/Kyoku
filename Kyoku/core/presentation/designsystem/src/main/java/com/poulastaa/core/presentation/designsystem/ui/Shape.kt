package com.poulastaa.core.presentation.designsystem.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.toPxf

val AppShape = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(52.dp)
)

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