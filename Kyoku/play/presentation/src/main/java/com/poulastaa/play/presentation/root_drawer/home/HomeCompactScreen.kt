package com.poulastaa.play.presentation.root_drawer.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.play.domain.TopBarToDrawerEvent
import com.poulastaa.play.presentation.add_as_playlist.AddAsPlaylistRootScreen
import com.poulastaa.play.presentation.add_as_playlist.PlaylistBottomSheet
import com.poulastaa.play.presentation.root_drawer.home.components.GridImageCard
import com.poulastaa.play.presentation.root_drawer.home.components.HomeAlbumCard
import com.poulastaa.play.presentation.root_drawer.home.components.HomeAppbar
import com.poulastaa.play.presentation.root_drawer.home.components.HomeItemBottomSheet
import com.poulastaa.play.presentation.root_drawer.home.components.MoreFromArtist
import com.poulastaa.play.presentation.root_drawer.home.components.SavedAlbumCard
import com.poulastaa.play.presentation.root_drawer.home.components.SavedPlaylistCard
import com.poulastaa.play.presentation.root_drawer.home.components.SingleSongCard
import com.poulastaa.play.presentation.root_drawer.home.components.SuggestedArtistCard
import com.poulastaa.play.presentation.root_drawer.home.components.ViewMore
import com.poulastaa.play.presentation.root_drawer.model.HomeItemClickType
import kotlinx.coroutines.launch

@Composable
fun HomeCompactScreen(
    profileUrl: String,
    viewModel: HomeViewModel = hiltViewModel(),
    navigate: (HomeOtherScreens) -> Unit,
    onEvent: (TopBarToDrawerEvent) -> Unit,
) {
    val context = LocalContext.current

    ObserveAsEvent(flow = viewModel.uiEvent) {
        when (it) {
            is HomeUiAction.EmitToast -> Toast.makeText(
                context,
                it.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is HomeUiAction.Navigate -> navigate(it.screen)
        }
    }

    HomeScreen(
        profileUrl = profileUrl,
        state = viewModel.state,
        onProfileClick = {
            onEvent(TopBarToDrawerEvent.PROFILE_CLICK)
        },
        onSearchClick = {
            onEvent(TopBarToDrawerEvent.SEARCH_CLICK)
        },
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    state: HomeUiState,
    profileUrl: String,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val appBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val config = LocalConfiguration.current
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val itemBottomSheetState = rememberModalBottomSheetState()
    val playlistBottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()


    Row {
        Scaffold(
            modifier = Modifier
                .then(
                    if (config.screenWidthDp > 980 &&
                        state.playlistBottomSheetUiState.isOpen
                    ) Modifier
                        .fillMaxWidth(.6f)
                        .fillMaxHeight()
                    else Modifier.fillMaxSize()
                )
                .nestedScroll(appBarScrollBehavior.nestedScrollConnection),
            topBar = {
                HomeAppbar(
                    scrollBehavior = appBarScrollBehavior,
                    title = state.heading,
                    profileUrl = profileUrl,
                    onProfileClick = onProfileClick,
                    onSearchClick = onSearchClick
                )
            }
        ) { paddingValues ->
            if (!state.canShowUi) Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(70.dp),
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 2.dp
                )
            } else LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(paddingValues),
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
                                    .clickable {
                                        onEvent(
                                            HomeUiEvent.OnItemClick(
                                                playlist.id,
                                                HomeItemClickType.SAVED_PLAYLIST
                                            )
                                        )
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
                }

                if (state.savedAlbums.isNotEmpty()) item {
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
                        state.savedAlbums.take(
                            if (config.screenWidthDp < 500) 2
                            else state.savedAlbums.size
                        ).forEach { album ->
                            SavedAlbumCard(
                                header = state.header,
                                uiAlbum = album,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        onEvent(
                                            HomeUiEvent.OnItemClick(
                                                album.id,
                                                HomeItemClickType.SAVED_ALBUM
                                            )
                                        )
                                    }
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
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

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
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)

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
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)

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
                            .clip(CircleShape)
                            .height(60.dp)
                            .padding(end = MaterialTheme.dimens.small2)
                            .clickable {
                                onEvent(
                                    HomeUiEvent.ItemBottomSheetUiEvent.ExploreArtist(
                                        id = songData.artist.id
                                    )
                                )
                            },
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
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)

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
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(150.dp)
                                    .clickable {
                                        onEvent(
                                            HomeUiEvent.ItemBottomSheetUiEvent.ExploreArtist(
                                                id = songData.artist.id
                                            )
                                        )
                                    }
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
        }

        AnimatedVisibility(
            visible = config.screenWidthDp > 980 && state.playlistBottomSheetUiState.isOpen,
            enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
            exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
        ) {
            AddAsPlaylistRootScreen(
                modifier = Modifier
                    .padding(start = MaterialTheme.dimens.small2)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(top = 56.dp)
                    .padding(horizontal = MaterialTheme.dimens.medium1)
                    .fillMaxSize(),
                exploreType = state.playlistBottomSheetUiState.exploreType,
                navigateBack = {
                    onEvent(HomeUiEvent.PlaylistBottomSheetUiEvent.Cancel)
                }
            )
        }
    }

    LaunchedEffect(
        key1 = state.playlistBottomSheetUiState.isOpen,
        key2 = config.screenWidthDp < 980
    ) {
        if (state.playlistBottomSheetUiState.isOpen &&
            config.screenWidthDp < 980
        ) scope.launch {
            playlistBottomSheetState.show()
        }
    }

    if (state.playlistBottomSheetUiState.isOpen && config.screenWidthDp < 980) PlaylistBottomSheet( // medium screen
        sheetState = playlistBottomSheetState,
        exploreType = state.playlistBottomSheetUiState.exploreType,
        closeBottomSheet = {
            scope.launch {
                playlistBottomSheetState.hide()
            }.invokeOnCompletion {
                onEvent(HomeUiEvent.PlaylistBottomSheetUiEvent.Cancel)
            }
        }
    )


    LaunchedEffect(key1 = state.itemBottomSheetUiState.isOpen) {
        if (state.itemBottomSheetUiState.isOpen) scope.launch {
            itemBottomSheetState.show()
        }
    }

    if (state.itemBottomSheetUiState.isOpen) HomeItemBottomSheet(
        sheetState = itemBottomSheetState,
        header = state.header,
        state = state.itemBottomSheetUiState,
        onEvent = { event ->
            scope.launch {
                itemBottomSheetState.hide()
            }.invokeOnCompletion {
                onEvent(event)
            }
        },
        onCancel = {
            scope.launch {
                itemBottomSheetState.hide()
            }.invokeOnCompletion {
                onEvent(HomeUiEvent.ItemBottomSheetUiEvent.Cancel)
            }
        }
    )
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