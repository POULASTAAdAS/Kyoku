package com.poulastaa.play.presentation.player.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun PlayerCustomIconButton(
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
        enabled = enabled
    ) {
        Icon(
            modifier = modifier,
            imageVector = icon,
            contentDescription = null
        )
    }
}