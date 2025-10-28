package com.poulastaa.board.presentation.import_playlist.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.poulastaa.board.presentation.R
import com.poulastaa.board.presentation.import_playlist.ImportPlaylistUiAction
import com.poulastaa.board.presentation.import_playlist.UiPreviewPlaylist
import com.poulastaa.core.presentation.noRippleClickable
import com.poulastaa.core.presentation.shimmerEffect
import com.poulastaa.core.presentation.ui.DropDownArrowIcon
import com.poulastaa.core.presentation.ui.MusicIcon
import com.poulastaa.core.presentation.ui.dimens

@Composable
internal fun PlaylistCard(
    playlist: UiPreviewPlaylist,
    onAction: (ImportPlaylistUiAction) -> Unit,
) = Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ),
    shape = if (playlist.isExpanded) MaterialTheme.shapes.medium
    else MaterialTheme.shapes.large,
    elevation = CardDefaults.cardElevation(
        defaultElevation = 8.dp
    )
) {
    Card(
        shape = MaterialTheme.shapes.medium.copy(
            bottomEnd = CircleShape.bottomEnd,
            bottomStart = CircleShape.bottomStart,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.small3)
                .padding(horizontal = MaterialTheme.dimens.small2)
                .noRippleClickable(
                    onClick = {
                        onAction(ImportPlaylistUiAction.OnItemStateToggle(playlist.id))
                    }
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = playlist.title,
                modifier = Modifier.weight(1f),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.SemiBold
            )

            Icon(
                imageVector = DropDownArrowIcon,
                contentDescription = stringResource(R.string.drop_down),
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        onClick = {
                            onAction(
                                ImportPlaylistUiAction.OnItemStateToggle(
                                    id = playlist.id
                                )
                            )
                        }
                    )
                    .rotate(if (playlist.isExpanded) 180f else 0f),
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }

    AnimatedVisibility(
        visible = playlist.isExpanded,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(MaterialTheme.dimens.small2))

            playlist.songs.forEach { song ->
                Card(
                    modifier = Modifier
                        .heightIn(max = 80.dp)
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.small3),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .fillMaxHeight()
                                .clip(MaterialTheme.shapes.extraSmall)
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            SubcomposeAsyncImage(
                                model = song.coverImage,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                loading = {
                                    Box(Modifier.fillMaxSize()) {
                                        Box(
                                            Modifier
                                                .fillMaxSize()
                                                .shimmerEffect(
                                                    MaterialTheme.colorScheme.tertiary.copy(
                                                        alpha = .5f
                                                    )
                                                )
                                        )
                                    }
                                },
                                error = {

                                    Card(
                                        modifier = Modifier.fillMaxSize(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 3.dp
                                        ),
                                        shape = MaterialTheme.shapes.extraSmall
                                    ) {
                                        Box(
                                            Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = MusicIcon,
                                                contentDescription = null,
                                                modifier = Modifier.fillMaxSize(.7f),
                                                tint = MaterialTheme.colorScheme.tertiary
                                            )
                                        }
                                    }
                                }
                            )
                        }

                        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(
                                text = song.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            song.artist?.let {
                                Text(
                                    text = it,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(MaterialTheme.dimens.small2))
            }

            Spacer(Modifier.height(MaterialTheme.dimens.small2))
        }
    }
}