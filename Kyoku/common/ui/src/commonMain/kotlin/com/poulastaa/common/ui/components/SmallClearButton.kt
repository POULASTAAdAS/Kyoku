package com.poulastaa.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.poulastaa.common.ui.design_system.IconClose

@Composable
fun SmallClearButton(
    visible: Boolean,
    oClick: () -> Unit,
) {
    AnimatedVisibility(visible = visible) {
        Icon(
            imageVector = IconClose,
            contentDescription = "Clear",
            modifier = Modifier.clip(CircleShape)
                .clickable(onClick = oClick)
        )
    }
}