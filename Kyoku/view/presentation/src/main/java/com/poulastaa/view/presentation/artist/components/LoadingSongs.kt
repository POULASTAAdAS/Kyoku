package com.poulastaa.view.presentation.artist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.ThreeDotIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
internal fun LoadingSongs(itemCount: Int) {
    repeat(itemCount) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.aspectRatio(1f),
                shape = MaterialTheme.shapes.extraSmall,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .shimmerEffect(MaterialTheme.colorScheme.primary)
                )
            }

            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(.6f)
                        .height(24.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .shimmerEffect(MaterialTheme.colorScheme.primary)
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(.4f)
                        .height(16.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .shimmerEffect(MaterialTheme.colorScheme.primary)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = ThreeDotIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(.5f)
            )
        }

        Spacer(Modifier.height(MaterialTheme.dimens.small2))
    }
}