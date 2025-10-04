package com.poulastaa.auth.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

@Composable
internal fun ArchedScreen(
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BoxWithConstraints {
                val h = this.maxHeight / 3
                ArchBackground(height = h)
            }
        }

        content()
    }
}

@Composable
internal fun ArchBackground(
    modifier: Modifier = Modifier,
    height: Dp,
) {
    val color = MaterialTheme.colorScheme.primaryContainer
    val background = MaterialTheme.colorScheme.background

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val w = size.width
        val h = size.height

        val totalOffModeY = center.y / 4f

        drawCircle(
            color = color,
            radius = w / 1.5f,
            center = center.copy(
                y = center.y - totalOffModeY
            )
        )

        drawCircle(
            color = background,
            radius = w / 1.5f,
            center = Offset(x = w / 2, y = (h * 1.35f) - totalOffModeY)
        )
    }
}