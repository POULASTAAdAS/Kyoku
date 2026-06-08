package com.poulastaa.common.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poulastaa.common.ui.design_system.dimens

@Composable
fun ElevatedDefaultButton(
    width: Float = 0.6f,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier.animateContentSize().fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            modifier = Modifier.animateContentSize().fillMaxWidth(width),
            elevation = CardDefaults.cardElevation(
                defaultElevation = MaterialTheme.dimens.elevation.level3,
                pressedElevation = 0.dp,
            ),
            shape = MaterialTheme.shapes.small,
            colors = colors,
            enabled = enabled,
            onClick = onClick,
            content = content,
        )
    }
}