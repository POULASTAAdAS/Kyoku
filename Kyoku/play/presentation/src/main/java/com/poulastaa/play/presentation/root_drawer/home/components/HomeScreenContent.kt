package com.poulastaa.play.presentation.root_drawer.home.components

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.core.presentation.ui.model.UiPrevPlaylist
import com.poulastaa.play.presentation.root_drawer.home.HomeUiEvent
import com.poulastaa.play.presentation.root_drawer.home.HomeUiState
import com.poulastaa.play.presentation.root_drawer.home.model.UiArtistWithSong
import com.poulastaa.play.presentation.root_drawer.home.model.UiHomeData
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevAlbum
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevSong
import com.poulastaa.play.presentation.root_drawer.home.model.UiSongWithInfo
import com.poulastaa.play.presentation.root_drawer.model.HomeItemClickType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    profileUrl: String,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val appBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val config = LocalConfiguration.current
    val context = LocalContext.current

    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.nestedScroll(appBarScrollBehavior.nestedScrollConnection),
        topBar = {
            HomeAppbar(
                scrollBehavior = appBarScrollBehavior,
                title = state.heading,
                profileUrl = profileUrl,
                onProfileClick = onProfileClick,
                onSearchClick = onSearchClick
            )
        }
    ) {
        if (!state.canShowUi) Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(it),
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(70.dp),
                strokeCap = StrokeCap.Round,
                strokeWidth = 2.dp
            )
        }
        else LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            item {}

            if (state.savedPlaylists.isNotEmpty()) item {
                TopRow(
                    modifier = Modifier
                        .height(if (config.screenWidthDp < 500) 65.dp else 90.dp)
                        .fillMaxWidth()
                        .windowInsetsPadding(
                            insets = WindowInsets(
                                left = MaterialTheme.dimens.medium1,
                                right = MaterialTheme.dimens.medium1
                            )
                        )
                ) {
                    state.savedPlaylists.take(
                        if (config.screenWidthDp < 500) 2
                        else state.savedPlaylists.size
                    ).forEach { playlist ->
                        SavedPlaylistCard(
                            header = state.header,
                            uiPlaylist = playlist,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
            }

            if (state.savedAlbums.isNotEmpty()) item {
                TopRow(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .windowInsetsPadding(
                            insets = WindowInsets(
                                left = MaterialTheme.dimens.medium1,
                                right = MaterialTheme.dimens.medium1
                            )
                        )
                ) {
                    state.savedAlbums.take(
                        if (config.screenWidthDp < 500) 2
                        else state.savedPlaylists.size
                    ).forEach { playlist ->
                        SavedAlbumCard(
                            header = state.header,
                            uiAlbum = playlist,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
            }

            item {
                HeadLine(text = stringResource(id = R.string.explore_more))

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                ItemsRow {
                    item {
                        GridImageCard(
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    onEvent(
                                        HomeUiEvent.OnItemClick(
                                            itemClickType = HomeItemClickType.POPULAR_SONG_MIX
                                        )
                                    )
                                },
                                onLongClick = {
                                    onEvent(
                                        HomeUiEvent.OnItemLongClick(
                                            itemClickType = HomeItemClickType.POPULAR_SONG_MIX,
                                            context = context
                                        )
                                    )
                                }
                            ),
                            size = 150.dp,
                            header = state.header,
                            urls = state.staticData.popularSongMix.map { entry ->
                                entry.coverImage
                            },
                            title = stringResource(id = R.string.popular_song_mix)
                        )
                    }

                    item {
                        GridImageCard(
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    onEvent(
                                        HomeUiEvent.OnItemClick(
                                            itemClickType = HomeItemClickType.OLD_GEM
                                        )
                                    )
                                },
                                onLongClick = {
                                    onEvent(
                                        HomeUiEvent.OnItemLongClick(
                                            itemClickType = HomeItemClickType.OLD_GEM,
                                            context = context
                                        )
                                    )
                                }
                            ),
                            size = 150.dp,
                            header = state.header,
                            urls = state.staticData.popularSongFromYourTime.map { entry ->
                                entry.coverImage
                            },
                            title = stringResource(id = R.string.popular_songs_from_your_time)
                        )
                    }

                    item {
                        GridImageCard(
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    onEvent(
                                        HomeUiEvent.OnItemClick(
                                            itemClickType = HomeItemClickType.FAVOURITE_ARTIST_MIX
                                        )
                                    )
                                },
                                onLongClick = {
                                    onEvent(
                                        HomeUiEvent.OnItemLongClick(
                                            itemClickType = HomeItemClickType.FAVOURITE_ARTIST_MIX,
                                            context = context
                                        )
                                    )
                                }
                            ),
                            size = 150.dp,
                            header = state.header,
                            urls = state.staticData.favouriteArtistMix.map { entry ->
                                entry.coverImage
                            },
                            title = stringResource(id = R.string.popular_artist_mix)
                        )
                    }
                }
            }

            item {}

            item {
                HeadLine(text = stringResource(id = R.string.artist_you_may_like))

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                ItemsRow {
                    items(
                        items = state.staticData.suggestedArtist,
                        key = { artist ->
                            artist.id
                        }
                    ) { artist ->
                        SuggestedArtistCard(
                            modifier = Modifier
                                .size(220.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .combinedClickable(
                                    onClick = {
                                        onEvent(
                                            HomeUiEvent.OnItemClick(
                                                id = artist.id,
                                                itemClickType = HomeItemClickType.SUGGEST_ARTIST
                                            )
                                        )
                                    },
                                    onLongClick = {
                                        onEvent(
                                            HomeUiEvent.OnItemLongClick(
                                                id = artist.id,
                                                itemClickType = HomeItemClickType.SUGGEST_ARTIST,
                                                context = context
                                            )
                                        )
                                    }
                                ),
                            artist = artist,
                            header = state.header
                        )
                    }
                }
            }

            item {}

            item {
                HeadLine(text = stringResource(id = R.string.popular_albums))

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                ItemsRow {
                    items(
                        items = state.staticData.popularAlbum,
                        key = { album ->
                            album.id
                        }
                    ) { album ->
                        HomeAlbumCard(
                            modifier = Modifier
                                .size(150.dp)
                                .combinedClickable(
                                    onClick = {
                                        onEvent(
                                            HomeUiEvent.OnItemClick(
                                                id = album.id,
                                                itemClickType = HomeItemClickType.SUGGEST_ALBUM
                                            )
                                        )
                                    },
                                    onLongClick = {
                                        onEvent(
                                            HomeUiEvent.OnItemLongClick(
                                                id = album.id,
                                                itemClickType = HomeItemClickType.SUGGEST_ALBUM,
                                                context = context
                                            )
                                        )
                                    }
                                ),
                            header = state.header,
                            prevAlbum = album,
                        )
                    }

                    item {
                        ViewMore(
                            modifier = Modifier.size(150.dp)
                        )
                    }
                }
            }

            item {}

            item {
                HeadLine(text = stringResource(id = R.string.best_from_artist))
            }

            items(
                items = state.staticData.popularArtistSong,
                key = { artist ->
                    artist.artist.id
                }
            ) { songData ->
                MoreFromArtist(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(end = MaterialTheme.dimens.small2),
                    header = state.header,
                    artist = songData.artist
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

                ItemsRow {
                    items(
                        items = songData.listOfSong,
                        key = { song ->
                            song.id
                        }
                    ) { song ->
                        SingleSongCard(
                            modifier = Modifier
                                .size(150.dp)
                                .combinedClickable(
                                    onClick = {
                                        onEvent(
                                            HomeUiEvent.OnItemClick(
                                                id = song.id,
                                                itemClickType = HomeItemClickType.SUGGEST_ARTIST_SONG
                                            )
                                        )
                                    },
                                    onLongClick = {
                                        onEvent(
                                            HomeUiEvent.OnItemLongClick(
                                                id = song.id,
                                                artistId = songData.artist.id,
                                                itemClickType = HomeItemClickType.SUGGEST_ARTIST_SONG,
                                                context = context
                                            )
                                        )
                                    }
                                ),
                            header = state.header,
                            song = song
                        )
                    }

                    item {
                        ViewMore(
                            modifier = Modifier.size(150.dp)
                        )
                    }
                }
            }

            item {
                Spacer(
                    modifier = Modifier.height(
                        if (config.screenWidthDp < 500) 70.dp
                        else MaterialTheme.dimens.medium1
                    )
                )
            }
        }


        LaunchedEffect(key1 = state.bottomSheetUiState.isOpen) {
            if (state.bottomSheetUiState.isOpen) scope.launch {
                bottomSheetState.show()
            }
        }

        if (state.bottomSheetUiState.isOpen) ItemBottomSheet(
            sheetState = bottomSheetState,
            header = state.header,
            state = state.bottomSheetUiState,
            onEvent = { event ->
                scope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    onEvent(event)
                }
            },
            onCancel = {
                scope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    onEvent(HomeUiEvent.BottomSheetUiEvent.Cancel)
                }
            }
        )
    }
}


@Composable
private fun ItemsRow(
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium2),
        contentPadding = PaddingValues(
            start = MaterialTheme.dimens.medium1,
            end = MaterialTheme.dimens.medium1
        )
    ) {
        content()
    }
}

@Composable
private fun HeadLine(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.SemiBold,
        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
        modifier = Modifier.windowInsetsPadding(
            insets = WindowInsets(
                left = MaterialTheme.dimens.medium1,
            )
        )
    )
}

@Composable
private fun TopRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
        content = content
    )
}

@PreviewLightDark
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 840,
    heightDp = 540
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 840,
    heightDp = 360
)
@Preview(
    widthDp = 840,
    heightDp = 360
)
@Preview(
    widthDp = 840,
    heightDp = 540
)
@Composable
private fun Preview() {
    AppThem {
        HomeScreen(
            state = HomeUiState(
                heading = "Good Morning",
                isNewUser = false,
                isDataLoading = false,
                header = "",
                staticData = prevData,
                savedPlaylists = (1..3).map {
                    UiPrevPlaylist(
                        id = it.toLong(),
                        name = "Playlist $it",
                        urls = emptyList()
                    )
                },
                savedAlbums = (1..3).map {
                    UiPrevAlbum(
                        id = it.toLong(),
                        name = "Album $it",
                    )
                }
            ),
            profileUrl = "",
            onProfileClick = { /*TODO*/ },
            onSearchClick = { /*TODO*/ },
            onEvent = {}
        )
    }
}

private val prevData = UiHomeData(
    popularSongMix = listOf(
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        )
    ),
    popularSongFromYourTime = listOf(
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        )
    ),
    favouriteArtistMix = listOf(
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        )
    ),
    dayTypeSong = listOf(
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        ),
        UiPrevSong(
            id = 1,
            coverImage = ""
        )
    ),
    popularAlbum = (1..7).map {
        UiPrevAlbum(
            id = it.toLong(),
            name = "Album $it",
            coverImage = ""
        )
    },
    suggestedArtist = (1..7).map {
        UiArtist(
            id = it.toLong(),
            name = "Artist $it",
            coverImageUrl = ""
        )
    },
    popularArtistSong = (1..4).map {
        UiArtistWithSong(
            UiArtist(
                id = it.toLong(),
                name = "Artist $it",
                coverImageUrl = ""
            ),
            listOfSong = (1..7).map {
                UiSongWithInfo(
                    id = it.toLong(),
                    title = "Song $it",
                    coverImage = ""
                )
            }
        )
    }
)