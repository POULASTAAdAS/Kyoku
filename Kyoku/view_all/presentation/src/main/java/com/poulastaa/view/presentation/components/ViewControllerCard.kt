package com.poulastaa.view.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.PlayType
import com.poulastaa.core.presentation.designsystem.ui.PlayIcon
import com.poulastaa.core.presentation.designsystem.ui.ShuffleIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
internal fun ViewControllerCard(
    name: String,
    totalSongs: Int,
    isArtist: Boolean,
    textAlignment: Alignment.Horizontal = Alignment.Start,
    onExplore: () -> Unit,
    play: (type: PlayType) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (isArtist) {
            Details(
                modifier = Modifier.fillMaxWidth(),
                name = name,
                totalSongs = totalSongs,
                textAlignment = textAlignment,
            )
            Spacer(Modifier.height(MaterialTheme.dimens.small3))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isArtist) Card(
                modifier = Modifier.fillMaxWidth(.65f),
                onClick = onExplore,
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = "${stringResource(R.string.explore_artist)} $name",
                    color = MaterialTheme.colorScheme.primaryContainer,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.medium1)
                        .align(Alignment.CenterHorizontally),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            } else Details(
                name = name,
                totalSongs = totalSongs,
                textAlignment = textAlignment,
            )

            Spacer(Modifier.weight(1f))

            IconButton(
                onClick = {
                    play(PlayType.SHUFFLE)
                }
            ) {
                Icon(
                    imageVector = ShuffleIcon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(.7f)
                )
            }

            Spacer(Modifier.width(MaterialTheme.dimens.small1))

            Card(
                modifier = Modifier.size(70.dp),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                onClick = {
                    play(PlayType.DEFAULT)
                }
            ) {
                Icon(
                    imageVector = PlayIcon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun Details(
    modifier: Modifier = Modifier,
    name: String,
    totalSongs: Int,
    textAlignment: Alignment.Horizontal,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = textAlignment
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "Total songs: $totalSongs",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary.copy(.7f),
            fontSize = MaterialTheme.typography.titleSmall.fontSize
        )
    }
}