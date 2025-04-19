package com.poulastaa.setup.presentation.pic_artist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.AppThem

@Composable
internal fun ArtistLoadingCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .shimmerEffect(MaterialTheme.colorScheme.primary)
        )
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        ArtistLoadingCard(Modifier.size(80.dp))
    }
}