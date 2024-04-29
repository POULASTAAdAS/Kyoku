package com.poulastaa.kyoku.presentation.screen.song_view.playlist

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.poulastaa.kyoku.presentation.screen.song_view.common.poster
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens
import com.poulastaa.kyoku.utils.Constants.PLAYER_PADDING

@Composable
fun PlaylistScreen(
    data: SongViewUiModel,
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    poster: String,
    isSmallPhone: Boolean,
    playControl: (SongViewUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            contentPadding = PaddingValues(
                bottom = MaterialTheme.dimens.medium1 + PLAYER_PADDING
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

            poster(
                isDarkThem = isDarkThem,
                isCookie = isCookie,
                headerValue = headerValue,
                poster = poster,
                isSmallPhone = isSmallPhone
            )

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            }

            info(
                name = data.name,
                size = data.listOfSong.size,
                totalTime = data.totalTime
            )

            playControl(
                isDownloading = false, // todo
                onDownloadClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.DownloadClick(
                            name = data.name,
                            type = UiEvent.PlayType.PLAYLIST
                        )
                    )
                },
                onShuffleClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.ShuffleClick(
                            name = data.name,
                            type = UiEvent.PlayType.PLAYLIST
                        )
                    )
                },
                onPlayClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.PlayClick(
                            name = data.name,
                            type = UiEvent.PlayType.PLAYLIST
                        )
                    )
                }
            )

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
            }


            playlistSongs(
                isDarkThem = isDarkThem,
                isCookie = isCookie,
                headerValue = headerValue,
                data = data.listOfSong,
                onSongClick = { id ->
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.SongPlayClick(
                            name = data.name,
                            songId = id,
                            type = UiEvent.PlayType.PLAYLIST_SONG
                        )
                    )
                }
            )
        }
    }
}

private fun LazyListScope.playlistSongs(
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    data: List<UiPlaylistSong>,
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
@Composable
private fun Preview() {
    val list = ArrayList<UiPlaylistSong>()

    for (i in 1..10) {
        list.add(
            UiPlaylistSong(
                songId = 1,
                title = "Title $i",
                artist = "Artist $i",
                masterPlaylistUrl = "",
                coverImage = "",
                totalTime = ""
            )
        )
    }

    TestThem {
        PlaylistScreen(
            data = SongViewUiModel(
                name = "Playlist",
                listOfSong = list
            ),
            isSmallPhone = false,
            isDarkThem = isSystemInDarkTheme(),
            isCookie = false,
            headerValue = "",
            poster = "",
            navigateBack = {},
            playControl = {}
        )
    }
}