package com.poulastaa.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.poulastaa.common.ui.design_system.IconArrowBack
import com.poulastaa.common.ui.design_system.StringBackButton

@Composable
fun BackButton(
    rotation: Float = 0f,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary
    ),
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        colors = colors,
    ) {
        Box(
            Modifier.minimumInteractiveComponentSize()
                .padding(end = 4.dp)
                .rotate(rotation),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = IconArrowBack,
                contentDescription = StringBackButton
            )
        }
    }
}