package com.poulastaa.core.presentation

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.CircularReveal
import kotlinx.coroutines.delay

@Composable
fun circularConsumeAnimation(
    density: Density,
    config: Configuration,
    offset: Offset,
    animationTimeMileSec: Int,
    resetAnimation: () -> Unit,
): CircularReveal {
    val backgroundColor = MaterialTheme.colorScheme.background
    var animationOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    val revealSize = remember { Animatable(0f) }
    val screenWidthPx = with(density) { config.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(density) { config.screenHeightDp.dp.toPx() }

    var initialOffset by remember { mutableStateOf(offset) }

    LaunchedEffect(offset) {
        animationOffset = offset
        if (offset.x > 0f) {
            initialOffset = offset
            // Calculate maximum radius from touch point to screen corners
            val maxRadius = listOf(
                (animationOffset - Offset(0f, 0f)).getDistance(),
                (animationOffset - Offset(screenWidthPx, 0f)).getDistance(),
                (animationOffset - Offset(0f, screenHeightPx)).getDistance(),
                (animationOffset - Offset(screenWidthPx, screenHeightPx)).getDistance()
            ).maxOrNull() ?: 0f

            // Animate circle expansion
            revealSize.snapTo(0f)
            revealSize.animateTo(
                targetValue = maxRadius,
                animationSpec = tween(animationTimeMileSec)
            )
            // Reset offset after animation to trigger contraction
            resetAnimation()
        } else {
            animationOffset = initialOffset

            // Animate circle contraction when offset is reset
            revealSize.animateTo(
                targetValue = 0f,
                animationSpec = tween(animationTimeMileSec)
            )
        }

        delay(100) // Delay to stop screen flickering
    }

    return CircularReveal(
        backgroundColor = backgroundColor,
        revealSize = revealSize,
        center = animationOffset
    )
}