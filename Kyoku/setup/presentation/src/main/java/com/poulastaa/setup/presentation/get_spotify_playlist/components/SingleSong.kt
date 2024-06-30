package com.poulastaa.setup.presentation.get_spotify_playlist.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.DropDownIcon
import com.poulastaa.core.presentation.designsystem.MusicImage
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.getBitmapFromUrlOrCache
import com.poulastaa.setup.presentation.get_spotify_playlist.model.UiPlaylist
import com.poulastaa.setup.presentation.get_spotify_playlist.model.UiSong

@Composable
fun PlaylistCard(
    modifier: Modifier = Modifier,
    elevation: CardElevation,
    header: String,
    playlist: UiPlaylist,
    internalPadding: Dp,
    storeImageColor: (id: Long, bitmap: Bitmap) -> Unit,
) {
    Card(
        modifier = modifier,
        elevation = elevation,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(internalPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.small3),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = playlist.name,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.SemiBold
                )

                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(if (playlist.isExpanded) 180f else 0f),
                    imageVector = DropDownIcon,
                    contentDescription = null,
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.dimens.small3)
                    .height(1.5.dp)
                    .padding(horizontal = MaterialTheme.dimens.small3)
                    .background(color = MaterialTheme.colorScheme.primaryContainer.copy(.6f))
            )

            AnimatedVisibility(visible = playlist.isExpanded) {
                val temp = remember {
                    playlist.isExpanded
                }

                if (temp) Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
                ) {
                    playlist.listOfUiSong.forEach { song ->
                        SingleSong(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            header = header,
                            title = song.title,
                            artist = song.artist,
                            coverImageUrl = song.coverImage,
                            storeImageColor = {
                                it?.let {
                                    storeImageColor(song.id, it)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun SingleSong(
    modifier: Modifier = Modifier,
    header: String,
    title: String,
    artist: String,
    coverImageUrl: String,
    storeImageColor: (Bitmap?) -> Unit,
) {
    val bitmapState = getBitmapFromUrlOrCache(url = coverImageUrl, header = header)
    val bitmap = bitmapState.value

    LaunchedEffect(key1 = bitmap != null) {
        storeImageColor(bitmap)
    }

    Row(
        modifier = modifier
            .padding(MaterialTheme.dimens.small3)
    ) {
//        if (bitmap != null) {
//            Image(
//                bitmap = bitmap.asImageBitmap(),
//                contentDescription = null,
//                contentScale = ContentScale.Inside,
//                modifier = Modifier
//                    .clip(CircleShape)
//                    .aspectRatio(1f)
//                    .border(
//                        width = 1.2.dp,
//                        color = MaterialTheme.colorScheme.primary.copy(.3f),
//                        shape = CircleShape
//                    )
//            )
//        } else {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(20.dp),
//                    strokeWidth = 1.5.dp,
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//            }
//        }
//        SubcomposeAsyncImage(
//            model = bitmap,
//            contentDescription = null,
//            contentScale = ContentScale.Inside,
//            modifier = Modifier
//                .clip(CircleShape)
//                .aspectRatio(1f)
//                .border(
//                    width = 1.2.dp,
//                    color = MaterialTheme.colorScheme.primary.copy(.3f),
//                    shape = CircleShape
//                ),
//            loading = {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(20.dp),
//                        strokeWidth = 1.5.dp,
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
//                }
//            },
//            error = {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Image(
//                        painter = MusicImage,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(MaterialTheme.dimens.medium1),
//                        alignment = Alignment.Center,
//                        colorFilter = ColorFilter.tint(
//                            color = MaterialTheme.colorScheme.onBackground
//                        )
//                    )
//                }
//            }
//        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = artist,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.onSurface.copy(.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(
    uiMode = UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    var expanded by remember {
        mutableStateOf(true)
    }

    val playlist = (1..7).map {
        UiSong(
            id = it.toLong(),
            title = "Song Title $it",
            artist = "Artist1 , Artist 2",
            coverImage = ""
        )
    }.let {
        UiPlaylist(
            id = 1,
            name = "Some Playlist",
            listOfUiSong = it,
            isExpanded = expanded
        )
    }
    AppThem {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .fillMaxSize()
                .padding(MaterialTheme.dimens.medium1)
        ) {
            PlaylistCard(
                modifier = Modifier.clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null,
                    onClick = {
                        expanded = !expanded
                    }
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                ),
                header = "",
                playlist = playlist,
                internalPadding = MaterialTheme.dimens.small3,
                storeImageColor = { _, _ -> }
            )
        }
    }
}