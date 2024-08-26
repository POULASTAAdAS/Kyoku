package com.poulastaa.play.presentation.view_artist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.FollowArtistIcon
import com.poulastaa.core.presentation.designsystem.components.ShimmerSongCard
import com.poulastaa.core.presentation.designsystem.components.shimmerEffect
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun ViewArtistCompactLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = MaterialTheme.dimens.medium1)
            .verticalScroll(rememberScrollState())
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .then(
                    if (LocalConfiguration.current.screenWidthDp > 680) Modifier
                        .size(400.dp)
                    else Modifier
                        .aspectRatio(1f)
                        .padding(MaterialTheme.dimens.large2)
                ),
            shape = MaterialTheme.shapes.small,
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(.7f)
                    .height(40.dp),
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

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {},
                modifier = Modifier
                    .clip(CircleShape)
                    .size(35.dp)
                    .shimmerEffect()
            ) {
                Icon(
                    imageVector = FollowArtistIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(.3f),
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        Card(
            modifier = Modifier
                .fillMaxWidth(.35f)
                .height(25.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerEffect()
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))

        Card(
            modifier = Modifier
                .fillMaxWidth(.5f)
                .height(45.dp)
                .align(Alignment.CenterHorizontally),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerEffect()
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))

        repeat(6) {
            ShimmerSongCard(
                modifier = Modifier.height(90.dp)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
        }

        Card(
            modifier = Modifier
                .fillMaxWidth(.5f)
                .height(45.dp)
                .align(Alignment.CenterHorizontally),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerEffect()
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        ViewArtistCompactLoading()
    }
}