package com.poulastaa.kyoku.presentation.screen.song_view.artist

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.kyoku.data.model.UiEvent
import com.poulastaa.kyoku.data.model.api.service.home.SongPreview
import com.poulastaa.kyoku.data.model.screens.song_view.SongViewUiEvent
import com.poulastaa.kyoku.data.model.screens.song_view.UiArtist
import com.poulastaa.kyoku.presentation.screen.song_view.common.ArtistSongCard
import com.poulastaa.kyoku.presentation.screen.song_view.common.navigateBackButton
import com.poulastaa.kyoku.presentation.screen.song_view.common.playControl
import com.poulastaa.kyoku.presentation.screen.song_view.common.poster
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens
import com.poulastaa.kyoku.utils.Constants

@Composable
fun ArtistScreen(
    data: UiArtist,
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    isSmallPhone: Boolean,
    navigateBack: () -> Unit,
    playControl: (SongViewUiEvent) -> Unit,
    viewAll: (SongViewUiEvent.ItemClick.ViewAllFromArtist) -> Unit
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
                poster = data.coverImage,
                isSmallPhone = isSmallPhone
            )

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            }

            artistInfo(
                name = data.name,
                times = data.points,
                onClick = {
                    viewAll.invoke(
                        SongViewUiEvent.ItemClick.ViewAllFromArtist(name = data.name)
                    )
                }
            )

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
            }

            playControl(
                isDownloading = false, // todo
                onDownloadClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.DownloadClick(
                            name = data.name,
                            type = UiEvent.PlayType.ARTIST_MORE_ALL_SONG
                        )
                    )
                },
                onShuffleClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.ShuffleClick(
                            name = data.name,
                            type = UiEvent.PlayType.ARTIST_MORE_ALL_SONG
                        )
                    )
                },
                onPlayClick = {
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.PlayClick(
                            name = data.name,
                            type = UiEvent.PlayType.ARTIST_MORE_ALL_SONG
                        )
                    )
                }
            )

            artistSongs(
                isDarkThem = isDarkThem,
                isCookie = isCookie,
                headerValue = headerValue,
                data = data.listOfSong,
                onSongClick = { id ->
                    playControl.invoke(
                        SongViewUiEvent.PlayControlClick.SongPlayClick(
                            name = data.name,
                            songId = id,
                            type = UiEvent.PlayType.ARTIST_MORE_ONE_SONG
                        )
                    )
                }
            )

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedButton(
                        onClick = {
                            viewAll.invoke(SongViewUiEvent.ItemClick.ViewAllFromArtist(data.name))
                        },
                        shape = MaterialTheme.shapes.medium,
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 0.8.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.inverseSurface
                                )
                            )
                        )
                    ) {
                        Text(
                            text = "view all from ${data.name}",
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        }
    }
}

private fun LazyListScope.artistInfo(
    name: String,
    times: Long,
    onClick: (String) -> Unit
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    start = MaterialTheme.dimens.medium1,
                    end = MaterialTheme.dimens.medium1
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(.5f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = name,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "$times times listened",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                OutlinedButton(
                    onClick = {
                        onClick.invoke(name)
                    },
                    shape = MaterialTheme.shapes.medium,
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 0.8.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
                ) {
                    Text(
                        text = "all from $name",
                        fontWeight = FontWeight.Light,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

private fun LazyListScope.artistSongs(
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    data: List<SongPreview>,
    onSongClick: (id: Long) -> Unit
) {
    items(data.size) {
        ArtistSongCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(
                    start = MaterialTheme.dimens.medium1,
                )
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable {
                    onSongClick.invoke(data[it].id.toLong())
                },
            isPlaying = data[it].isPlaying ?: false,
            isDarkThem = isDarkThem,
            isCookie = isCookie,
            headerValue = headerValue,
            title = data[it].title,
            artist = data[it].points.toString(),
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
        val listOfSong = ArrayList<SongPreview>()

        for (i in 1..3) {
            listOfSong.add(
                SongPreview(
                    id = i.toString(),
                    title = "Title $i",
                    artist = "Artist $i",
                    album = "Album $i",
                    coverImage = "",
                    points = (i * 100).toLong()
                )
            )
        }

        ArtistScreen(
            data = UiArtist(
                name = "Artist",
                coverImage = "",
                points = 100,
                listOfSong = listOfSong
            ),
            isDarkThem = isSystemInDarkTheme(),
            isCookie = false,
            headerValue = "",
            isSmallPhone = false,
            viewAll = {},
            playControl = {},
            navigateBack = {}
        )
    }
}