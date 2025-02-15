package com.poulastaa.core.presentation.designsystem.bottom_bar

import com.poulastaa.core.presentation.designsystem.ui.ShapeCornerRadius
import com.poulastaa.core.presentation.designsystem.ui.shapeCornerRadius

internal data class IndentShapeData(
    val xIndent: Float = 0f,
    val height: Float = 0f,
    val width: Float = 0f,
    val cornerRadius: ShapeCornerRadius = shapeCornerRadius(0f),
    val ballOffset: Float = 0f,
)