package com.poulastaa.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poulastaa.common.ui.design_system.IconArrowBack

@Composable
fun BackButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(
            Modifier.minimumInteractiveComponentSize()
                .padding(end = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = IconArrowBack,
                contentDescription = "Back Button"
            )
        }
    }
}