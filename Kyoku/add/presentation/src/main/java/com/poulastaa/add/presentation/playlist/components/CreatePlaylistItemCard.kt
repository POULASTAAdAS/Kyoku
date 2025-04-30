package com.poulastaa.add.presentation.playlist.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.add.presentation.playlist.AddSongToPlaylistUiItem
import com.poulastaa.add.presentation.playlist.AddToPlaylistItemUiType
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ui.AddIcon
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.ui.SongIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
internal fun CreatePlaylistItemCard(
    item: AddSongToPlaylistUiItem,
    onAction: () -> Unit,
    haptic: HapticFeedback,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.aspectRatio(1f),
            shape = if (item.type == AddToPlaylistItemUiType.ARTIST) CircleShape
            else MaterialTheme.shapes.extraSmall,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            if (item.type != AddToPlaylistItemUiType.PLAYLIST) ImageCard(
                modifier = Modifier.fillMaxSize(),
                poster = item.poster.firstOrNull()
            ) else Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.5f)
                ) {
                    ImageCard(
                        Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight(),
                        item.poster.getOrNull(0)
                    )

                    ImageCard(
                        Modifier.fillMaxSize(),
                        item.poster.getOrNull(1)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ImageCard(
                        Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight(),
                        item.poster.getOrNull(2)
                    )

                    ImageCard(
                        Modifier.fillMaxSize(),
                        item.poster.getOrNull(3)
                    )
                }
            }
        }

        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier
                .fillMaxWidth(.85f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.title,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary,
            )

            item.artist?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            }
        }

        Spacer(Modifier.weight(1f))

        if (item.type == AddToPlaylistItemUiType.SONG) OutlinedCard(
            shape = CircleShape,
            onClick = {
                onAction()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            },
            border = BorderStroke(
                width = 1.4.dp,
                color = MaterialTheme.colorScheme.primary
            ),
            colors = CardDefaults.outlinedCardColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = AddIcon,
                contentDescription = null
            )
        } else IconButton(
            onClick = onAction
        ) {
            Icon(
                imageVector = ArrowBackIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxSize(.8f)
                    .rotate(180f)
            )
        }
    }

    Spacer(Modifier.height(MaterialTheme.dimens.small1))
}

@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    poster: String?,
) {
    SubcomposeAsyncImage(
        model = CacheImageReq.imageReq(
            poster,
            LocalContext.current
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillBounds,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize(.4f),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.background
                )
            }
        },
        error = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = SongIcon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(.7f),
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            Box(
                Modifier
                    .padding(MaterialTheme.dimens.medium1),
                contentAlignment = Alignment.Center
            ) {
                CreatePlaylistItemCard(
                    item = AddSongToPlaylistUiItem(
                        title = "That Cool Playlist",
                        type = AddToPlaylistItemUiType.PLAYLIST
                    ),
                    onAction = {},
                    haptic = LocalHapticFeedback.current
                )
            }
        }
    }
}