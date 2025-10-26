package com.poulastaa.core.presentation

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

@Stable
fun Modifier.shimmerEffect(color: Color? = null) = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val startColor = color ?: MaterialTheme.colorScheme.secondary

    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(animation = tween(1_000)),
        label = "shimmer transition"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                startColor.copy(.7f),
                Color.Transparent
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned { size = it.size }
}


fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClick: () -> Unit,
) = this.clickable(
    enabled = enabled,
    indication = null,
    interactionSource = null,
    onClick = onClick
)

fun Modifier.noRippleCombineClickable(
    enabled: Boolean = true,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) = this.combinedClickable(
    indication = null,
    interactionSource = null,
    enabled = enabled,
    onLongClick = onLongClick,
    onClick = onClick
)

fun Modifier.isElementVisible(onVisibilityChanged: (Boolean) -> Unit) = composed {
    val isVisible by remember { derivedStateOf { mutableStateOf(false) } }
    LaunchedEffect(isVisible.value) {
        onVisibilityChanged.invoke(isVisible.value)
    }

    this.onGloballyPositioned { layoutCoordinates ->
        isVisible.value = layoutCoordinates.parentLayoutCoordinates?.let {
            val parentBounds = it.boundsInWindow()
            val childBounds = layoutCoordinates.boundsInWindow()
            parentBounds.overlaps(childBounds)
        } == true
    }
}