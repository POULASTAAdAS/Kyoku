package com.poulastaa.setup.presentation.pic_genre.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.shimmerEffect

@Composable
internal fun GenreLoadingCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp
        )
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .shimmerEffect()
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        GenreLoadingCard(
            Modifier
                .wrapContentWidth()
                .width(140.dp)
                .height(80.dp)
        )
    }
}