package com.poulastaa.main.presentation.main.components.utils

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

fun Modifier.noRippleClickable(
    onClick: () -> Unit,
) = composed {
    this.clickable(
        indication = null,
        interactionSource = null,
        onClick = onClick
    )
}

fun Modifier.ballTransform(ballAnimInfo: BallAnimInfo) = this
    .offset {
        IntOffset(
            x = ballAnimInfo.offset.x.toInt(),
            y = ballAnimInfo.offset.y.toInt()
        )
    }
    .graphicsLayer {
        scaleY = ballAnimInfo.scale
        scaleX = ballAnimInfo.scale
        transformOrigin = TransformOrigin(pivotFractionX = 0.5f, 0f)
    }

fun Modifier.rotationWithTopCenterAnchor(degrees: Float) = this
    .graphicsLayer(
        transformOrigin = TransformOrigin(
            pivotFractionX = 0.5f,
            pivotFractionY = 0.1f,
        ),
        rotationZ = degrees
    )


fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = "shimmer"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                MaterialTheme.colorScheme.secondary.copy(.7f),
                Color.Transparent,
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}