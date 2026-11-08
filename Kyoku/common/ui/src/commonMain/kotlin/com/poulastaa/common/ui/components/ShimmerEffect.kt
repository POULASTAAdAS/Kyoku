package com.poulastaa.common.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalWindowInfo

private const val SHIMMER_DURATION_MILLIS = 1600L
private const val SHIMMER_BAND_WIDTH_FRACTION = 0.6f

/**
 * Draws an animated shimmer over the element's bounds. Use it on placeholder shapes while
 * content is loading.
 *
 * Every shimmering element shares one band that sweeps across the whole window: the phase comes
 * from the frame clock (not a per-element animation start) and the band is placed in root
 * coordinates, so separate placeholders stay in sync.
 */
fun Modifier.shimmerEffect(): Modifier = composed {
    val baseColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val highlightColor = MaterialTheme.colorScheme.surfaceBright
    val windowWidth = LocalWindowInfo.current.containerSize.width.toFloat()

    val progress by produceState(0f) {
        while (true) withFrameMillis { frameMillis ->
            value = (frameMillis % SHIMMER_DURATION_MILLIS) / SHIMMER_DURATION_MILLIS.toFloat()
        }
    }
    var offsetInRoot by remember { mutableFloatStateOf(0f) }

    onGloballyPositioned { offsetInRoot = it.positionInRoot().x }
        .drawBehind {
            val bandWidth = windowWidth * SHIMMER_BAND_WIDTH_FRACTION
            // band start in root coordinates, moved into this element's local coordinates
            val bandStart = -bandWidth + progress * (windowWidth + bandWidth) - offsetInRoot

            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(baseColor, highlightColor, baseColor),
                    start = Offset(bandStart, 0f),
                    end = Offset(bandStart + bandWidth, 0f),
                ),
            )
        }
}
