package com.poulastaa.kyoku.presentation.screen.song_view.favourites

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.data.model.UiEvent
import com.poulastaa.kyoku.data.model.screens.song_view.SongViewUiEvent
import com.poulastaa.kyoku.data.model.screens.song_view.SongViewUiModel
import com.poulastaa.kyoku.data.model.screens.song_view.UiPlaylistSong
import com.poulastaa.kyoku.presentation.screen.song_view.common.SongCard
import com.poulastaa.kyoku.presentation.screen.song_view.common.info
import com.poulastaa.kyoku.presentation.screen.song_view.common.navigateBackButton
import com.poulastaa.kyoku.presentation.screen.song_view.common.playControl
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens
import com.poulastaa.kyoku.utils.Constants


@Composable
fun FavouriteScreen(
    data: SongViewUiModel,
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    isSmallPhone: Boolean,
    playControl: (SongViewUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.onBackground,
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background,
                        )
                    )
                )
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            contentPadding = PaddingValues(
                bottom = MaterialTheme.dimens.medium1 + Constants.PLAYER_PADDING
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            navigateBackButton {
                navigateBack.invoke()
            }

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
            }

            item {
                Card(
                    modifier = Modifier
                        .size(if (isSmallPhone) 200.dp else 240.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = null,
                        modifier = Modifier
                            .size(240.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer
                            )
                            .padding(MaterialTheme.dimens.large1),
                        tint = if (isDarkThem) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.inversePrimary
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            }

            info(
                name = "Favourites",
                size = data.listOfSong.size,
                totalTime = data.totalTime
            )

            playControl(
                isDownloading = false, // todo
                onDownloadClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.DownloadClick(
                            type = UiEvent.PlayType.FAVOURITE
                        )
                    )
                },
                onShuffleClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.ShuffleClick(
                            type = UiEvent.PlayType.FAVOURITE
                        )
                    )
                },
                onPlayClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.PlayClick(
                            type = UiEvent.PlayType.FAVOURITE
                        )
                    )
                }
            )

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
            }

            favouriteSongs(
                isDarkThem = isDarkThem,
                isCookie = isCookie,
                headerValue = headerValue,
                data = data.listOfSong,
                onSongClick = { id ->
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.SongPlayClick(
                            songId = id,
                            type = UiEvent.PlayType.FAVOURITE_SONG
                        )
                    )
                }
            )
        }
    }
}

private fun LazyListScope.favouriteSongs(
    data: List<UiPlaylistSong>,
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    onSongClick: (id: Long) -> Unit
) {
    items(data.size) {
        SongCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(start = MaterialTheme.dimens.small3)
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable {
                    onSongClick.invoke(data[it].songId)
                },
            isPlaying = data[it].isPlaying ?: false,
            isDarkThem = isDarkThem,
            isCookie = isCookie,
            headerValue = headerValue,
            title = data[it].title,
            artist = data[it].artist,
            coverImage = data[it].coverImage
        )
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    TestThem {
        val data = ArrayList<UiPlaylistSong>()

        for (i in 1..10) {
            data.add(
                UiPlaylistSong(
                    songId = i.toLong(),
                    title = "Title $i",
                    artist = "Artist $i",
                    coverImage = ""
                )
            )
        }

        FavouriteScreen(
            data = SongViewUiModel(
                name = "Favourite",
                totalTime = "120",
                listOfSong = data
            ),
            isDarkThem = isSystemInDarkTheme(),
            isCookie = false,
            headerValue = "",
            isSmallPhone = false,
            playControl = {}
        ) {

        }
    }
}