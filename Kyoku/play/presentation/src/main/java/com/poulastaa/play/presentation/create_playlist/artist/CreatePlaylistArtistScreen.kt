package com.poulastaa.play.presentation.create_playlist.artist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.CompactErrorScreen
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistPagingUiData
import com.poulastaa.play.presentation.create_playlist.artist.components.CreatePlaylistArtistTopBar
import com.poulastaa.play.presentation.create_playlist.components.CreatePlaylistAlbumCard
import com.poulastaa.play.presentation.create_playlist.components.CreatePlaylistSongCard
import com.poulastaa.play.presentation.create_playlist.toUiSong

@Composable
fun CreatePlaylistArtistRootScreen(
    modifier: Modifier = Modifier,
    viewModel: CreatePlaylistArtistViewModel = hiltViewModel(),
    artistId: Long,
    savedSongIdList: List<Long>,
    onEvent: (CreatePlaylistArtistUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    LaunchedEffect(key1 = artistId) {
        viewModel.init(artistId)
    }

    LaunchedEffect(key1 = savedSongIdList) {
        viewModel.updateSavedSongIdList(savedSongIdList)
    }

    CreatePlaylistArtistScreen(
        modifier = modifier,
        state = viewModel.state,
        album = viewModel.album.collectAsLazyPagingItems(),
        song = viewModel.song.collectAsLazyPagingItems(),
        onEvent = onEvent,
        onBackClick = {
            viewModel.clear()
            onBackClick()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreatePlaylistArtistScreen(
    modifier: Modifier = Modifier,
    state: CreatePlaylistArtistUiState,
    album: LazyPagingItems<CreatePlaylistPagingUiData>,
    song: LazyPagingItems<CreatePlaylistPagingUiData>,
    onEvent: (CreatePlaylistArtistUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            CreatePlaylistArtistTopBar(
                scrollBehavior = scroll,
                header = state.header,
                title = state.artist.name,
                coverImageUrl = state.artist.coverImageUrl,
                navigateBack = onBackClick
            )
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = state.loadingState, label = ""
        ) { loadingState ->
            when (loadingState) {
                DataLoadingState.LOADING -> {

                }

                DataLoadingState.LOADED -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(paddingValues)
                        .nestedScroll(scroll.nestedScrollConnection),
                    contentPadding = PaddingValues(MaterialTheme.dimens.medium1),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
                ) {
                    if (album.itemCount > 0) {
                        item {
                            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                            Text(
                                text = "${stringResource(id = R.string.album)}s",
                                fontWeight = FontWeight.Medium,
                                fontSize = MaterialTheme.typography.headlineSmall.fontSize
                            )

                            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

                            HorizontalDivider(
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.primary.copy(.5f)
                            )
                            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
                        }

                        items(album.itemCount) { index ->
                            album[index]?.let { album ->
                                CreatePlaylistAlbumCard(
                                    modifier = Modifier.clickable {
                                        onEvent(CreatePlaylistArtistUiEvent.OnAlbumClick(album.id))
                                    },
                                    header = state.header,
                                    data = album
                                )
                            }
                        }
                    }

                    if (song.itemCount > 0) {
                        item {
                            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                            Text(
                                text = stringResource(id = R.string.songs),
                                fontWeight = FontWeight.Medium,
                                fontSize = MaterialTheme.typography.headlineSmall.fontSize
                            )
                            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
                            HorizontalDivider(
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.primary.copy(.5f)
                            )

                            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
                        }

                        items(song.itemCount) { index ->
                            song[index]?.let { song ->
                                CreatePlaylistSongCard(
                                    modifier = Modifier.clickable {
                                        onEvent(CreatePlaylistArtistUiEvent.OnSongClick(song.id))
                                    },
                                    header = state.header,
                                    song = song.toUiSong()
                                )
                            }
                        }
                    }
                }

                DataLoadingState.ERROR -> CompactErrorScreen(
                    modifier = Modifier.padding(
                        paddingValues
                    )
                )
            }
        }
    }
}