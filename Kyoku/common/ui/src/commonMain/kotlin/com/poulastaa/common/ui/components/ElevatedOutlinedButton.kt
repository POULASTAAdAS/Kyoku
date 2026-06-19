package com.poulastaa.common.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poulastaa.common.ui.design_system.dimens

@Composable
fun ElevatedOutlinedButton(
    width: Float = 0.6f,
    borderWidth: Dp = 1.5.dp,
    borderColors: List<Color> = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.primaryContainer
    ),
    cardColors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.background
    ),
    shape: Shape = MaterialTheme.shapes.small,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier.animateContentSize().fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedCard(
            modifier = Modifier.animateContentSize().fillMaxWidth(width),
            elevation = CardDefaults.cardElevation(
                defaultElevation = MaterialTheme.dimens.elevation.level3,
                pressedElevation = 0.dp,
            ),
            border = BorderStroke(borderWidth, Brush.horizontalGradient(borderColors)),
            shape = shape,
            colors = cardColors,
            enabled = enabled,
            onClick = onClick,
            content = content,
        )
    }
}