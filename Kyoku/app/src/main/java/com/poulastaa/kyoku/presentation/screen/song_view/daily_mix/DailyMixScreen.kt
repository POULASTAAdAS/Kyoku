package com.poulastaa.kyoku.presentation.screen.song_view.daily_mix

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.poulastaa.kyoku.presentation.screen.song_view.common.SongCardNonDraggable
import com.poulastaa.kyoku.presentation.screen.song_view.common.info
import com.poulastaa.kyoku.presentation.screen.song_view.common.navigateBackButton
import com.poulastaa.kyoku.presentation.screen.song_view.common.playControl
import com.poulastaa.kyoku.presentation.screen.song_view.common.poster
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun DailyMixScreen(
    dailyMix: SongViewUiModel,
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
                bottom = MaterialTheme.dimens.medium1
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
                name = dailyMix.name,
                size = dailyMix.listOfSong.size,
                totalTime = dailyMix.totalTime
            )

            playControl(
                isDownloading = false, // todo
                onDownloadClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.DownloadClick(
                            type = UiEvent.PlayType.DAILY_MIX
                        )
                    )
                },
                onShuffleClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.ShuffleClick(
                            type = UiEvent.PlayType.DAILY_MIX
                        )
                    )
                },
                onPlayClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.PlayClick(
                            type = UiEvent.PlayType.DAILY_MIX
                        )
                    )
                }
            )

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
            }

            dailyMixSongs(
                isDarkThem = isDarkThem,
                isCookie = isCookie,
                headerValue = headerValue,
                data = dailyMix.listOfSong,
                onSongClick = { id ->
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.SongPlayClick(
                            songId = id,
                            type = UiEvent.PlayType.DAILY_MIX_SONG
                        )
                    )
                }
            )
        }
    }
}

private fun LazyListScope.dailyMixSongs(
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    data: List<UiPlaylistSong>,
    onSongClick: (id: Long) -> Unit
) {
    items(data.size) {
        SongCardNonDraggable(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(start = MaterialTheme.dimens.medium1)
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
//        val listOfSong = ArrayList<UiSong>()
//
//        for (i in 1..10) {
//            listOfSong.add(
//                UiSong(
//                    id = i.toLong(),
//                    title = "Title $i",
//                    artist = "Artist $i",
//                    album = "Album $i",
//                    coverImage = ""
//                )
//            )
//        }
//
//        DailyMixScreen(
//            dailyMix = listOfSong,
//            isDarkThem = isSystemInDarkTheme(),
//            isCookie = false,
//            headerValue = "",
//            poster = "",
//            isSmallPhone = false
//        ) {
//
//        }
    }
}