package com.poulastaa.kyoku.presentation.screen.song_view.album

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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.poulastaa.kyoku.utils.Constants

@Composable
fun AlbumScreen(
    isPrevAlbum:Boolean,
    album: SongViewUiModel,
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    poster: String,
    isSmallPhone: Boolean,
    state: LazyListState = rememberLazyListState(),
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
            state = state,
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
                name = album.name,
                size = album.listOfSong.size,
                totalTime = album.totalTime
            )

            playControl(
                isDownloading = false, // todo
                onDownloadClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.DownloadClick(
                            name = album.name,
                            type = if (isPrevAlbum) UiEvent.PlayType.ALBUM_PREV else UiEvent.PlayType.ALBUM
                        )
                    )
                },
                onShuffleClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.ShuffleClick(
                            name = album.name,
                            type = if (isPrevAlbum) UiEvent.PlayType.ALBUM_PREV else UiEvent.PlayType.ALBUM
                        )
                    )
                },
                onPlayClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.PlayClick(
                            name = album.name,
                            type = if (isPrevAlbum) UiEvent.PlayType.ALBUM_PREV else UiEvent.PlayType.ALBUM
                        )
                    )
                }
            )

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
            }

            albumSongs(
                isDarkThem = isDarkThem,
                isCookie = isCookie,
                headerValue = headerValue,
                data = album.listOfSong,
                onSongClick = { id ->
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.SongPlayClick(
                            name = album.name,
                            songId = id,
                            type = if (isPrevAlbum) UiEvent.PlayType.ALBUM_PREV_SONG else UiEvent.PlayType.ALBUM_SONG
                        )
                    )
                }
            )
        }
    }
}

private fun LazyListScope.albumSongs(
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
@Preview
@Composable
private fun Preview() {

    val list = ArrayList<UiPlaylistSong>()

    for (i in 1..10) {
        list.add(UiPlaylistSong())
    }

    val data = SongViewUiModel(
        name = "album",
        totalTime = "32",
        listOfSong = list
    )

    TestThem {
        AlbumScreen(
            isPrevAlbum = true,
            album = data,
            isDarkThem = isSystemInDarkTheme(),
            isCookie = false,
            headerValue = "",
            poster = "",
            isSmallPhone = false,
            playControl = {}
        ) {

        }
    }
}