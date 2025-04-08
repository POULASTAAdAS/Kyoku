package com.poulastaa.add.presentation.playlist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.shimmerEffect

@Composable
internal fun LoadingNavigationBall(
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    loadingColor: Color = MaterialTheme.colorScheme.primary,
) {
    Card(
        modifier = Modifier
            .size(12.dp)
            .aspectRatio(1f),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .shimmerEffect(loadingColor)
        )
    }
}