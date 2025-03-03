package com.poulastaa.setup.presentation.spotify_playlist.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.model.UiDetailedPrevSong
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ArrowDownIcon
import com.poulastaa.core.presentation.designsystem.ui.SongIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppCacheImage
import com.poulastaa.setup.presentation.spotify_playlist.UiPlaylist
import com.poulastaa.setup.presentation.spotify_playlist.UiPrevPlaylist

@Composable
internal fun SongList(
    item: UiPrevPlaylist,
    onPlaylistClick: (id: Long) -> Unit,
    headingPadding: Dp = MaterialTheme.dimens.small1,
    itemHeight: Dp = 100.dp,
    contentPadding: Dp = MaterialTheme.dimens.small2,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 0.dp
        ),
        onClick = {
            onPlaylistClick(item.playlist.id)
        }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(headingPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(.8f),
                    text = item.playlist.name,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = {
                        onPlaylistClick(item.playlist.id)
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(if (item.isExpanded) 180f else 0f),
                        imageVector = ArrowDownIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

        AnimatedContent(
            targetState = item.isExpanded,
            label = "playlist visibility animation"
        ) {
            if (it) {
                Column {
                    item.songs.forEach { song ->
                        Spacer(Modifier.height(contentPadding))

                        Card(
                            modifier = Modifier.padding(horizontal = contentPadding),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                            ),
                            shape = MaterialTheme.shapes.medium,
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(itemHeight)
                                    .padding(MaterialTheme.dimens.small2)
                                    .padding(end = MaterialTheme.dimens.small3)
                            ) {
                                AppCacheImage(
                                    url = song.poster,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .aspectRatio(1f)
                                        .border(
                                            width = 1.2.dp,
                                            color = MaterialTheme.colorScheme.primary.copy(.3f),
                                            shape = CircleShape
                                        ),
                                    errorIcon = SongIcon
                                )

                                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Row {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(.7f),
                                            text = song.title,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.background,
                                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Spacer(Modifier.weight(1f))

                                        song.releaseYear?.let { year ->
                                            Text(
                                                text = "Year: $year",
                                                fontWeight = FontWeight.Light,
                                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                                color = MaterialTheme.colorScheme.background
                                            )
                                        }
                                    }

                                    song.artists?.let { artist ->
                                        Text(
                                            text = artist,
                                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                            color = MaterialTheme.colorScheme.background,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}