package com.poulastaa.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ThreeDotIcon
import com.poulastaa.core.presentation.designsystem.dimens


@Composable
fun ShimmerSongCard(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.aspectRatio(1f),
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

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .align(Alignment.Start),
                shape = MaterialTheme.shapes.extraSmall,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmerEffect()
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))

            Card(
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .height(15.dp)
                    .align(Alignment.Start),
                shape = MaterialTheme.shapes.extraSmall,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmerEffect()
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                onClick = {},
            ) {
                Icon(
                    imageVector = ThreeDotIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.dimens.small1),
                    tint = MaterialTheme.colorScheme.onBackground.copy(.4f)
                )
            }
        }
    }
}