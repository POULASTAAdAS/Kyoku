package com.poulastaa.add.presentation.playlist.components

import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.AddIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
internal fun LoadingSongCard(
    modifier: Modifier,
    isAddButtonVisible: Boolean = true,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.aspectRatio(1f),
            shape = MaterialTheme.shapes.extraSmall,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .shimmerEffect(MaterialTheme.colorScheme.primaryContainer)
            )
        }

        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.8f),
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(.7f)
                    .fillMaxHeight(.2f),
                shape = MaterialTheme.shapes.extraSmall,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .shimmerEffect(MaterialTheme.colorScheme.primaryContainer)
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.small2))

            Card(
                modifier = Modifier
                    .fillMaxWidth(.4f)
                    .fillMaxHeight(.25f),
                shape = MaterialTheme.shapes.extraSmall,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .shimmerEffect(MaterialTheme.colorScheme.primaryContainer)
                )
            }
        }

        Spacer(Modifier.weight(1f))

        if (isAddButtonVisible) Icon(
            imageVector = AddIcon,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.primary.copy(.7f),
                    shape = CircleShape
                ),
            tint = MaterialTheme.colorScheme.primary.copy(.7f)
        )
    }
}