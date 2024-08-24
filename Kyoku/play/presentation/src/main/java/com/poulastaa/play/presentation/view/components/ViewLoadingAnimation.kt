package com.poulastaa.play.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.DownloadIcon
import com.poulastaa.core.presentation.designsystem.PlayIcon
import com.poulastaa.core.presentation.designsystem.ShuffleIcon
import com.poulastaa.core.presentation.designsystem.components.ShimmerSongCard
import com.poulastaa.core.presentation.designsystem.components.shimmerEffect
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun ColumnScope.ViewLoadingAnimation() {
    Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(MaterialTheme.dimens.large2),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmerEffect()
        )
    }

    Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))


    Card(
        modifier = Modifier
            .fillMaxWidth(.5f)
            .height(35.dp)
            .align(Alignment.Start),
        shape = MaterialTheme.shapes.extraSmall,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmerEffect()
        )
    }

    Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

    Card(
        modifier = Modifier
            .fillMaxWidth(.3f)
            .height(30.dp)
            .align(Alignment.Start),
        shape = MaterialTheme.shapes.extraSmall,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmerEffect()
        )
    }

    Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomButton(
            icon = DownloadIcon,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.weight(1f))

        CustomButton(
            icon = ShuffleIcon,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        IconButton(
            onClick = {},
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .shimmerEffect()
        ) {
            Icon(
                imageVector = PlayIcon,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                tint = MaterialTheme.colorScheme.onBackground.copy(.4f)
            )
        }
    }

    Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

    repeat(5) {
        ShimmerSongCard(modifier = Modifier.height(80.dp))

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
    }
}

@Composable
private fun CustomButton(
    modifier: Modifier = Modifier,
    icon: ImageVector
) {
    IconButton(
        onClick = {},
        modifier = modifier,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.small3),
            tint = MaterialTheme.colorScheme.onBackground.copy(.4f)
        )
    }
}






@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(MaterialTheme.dimens.medium1)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ViewLoadingAnimation()
        }
    }
}