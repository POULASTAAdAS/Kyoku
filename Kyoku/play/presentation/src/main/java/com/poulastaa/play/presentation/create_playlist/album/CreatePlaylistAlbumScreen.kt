package com.poulastaa.play.presentation.create_playlist.album

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.core.presentation.designsystem.components.CompactErrorScreen
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistPagingUiData
import com.poulastaa.play.presentation.create_playlist.components.CreatePlaylistSongCard
import com.poulastaa.play.presentation.create_playlist.toUiSong

@Composable
fun CreatePlaylistAlbumRootScreen(
    modifier: Modifier = Modifier,
    albumId: Long,
    savedSongIdList: List<Long>,
    viewModel: CreatePlaylistAlbumViewModel = hiltViewModel(),
    onEvent: (CreatePlaylistAlbumUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    LaunchedEffect(key1 = albumId) {
        viewModel.init(albumId, savedSongIdList)
    }

    CreatePlaylistAlbumScreen(
        modifier = modifier,
        state = viewModel.state,
        onEvent = {
            viewModel.onEvent(it)
            onEvent(it)
        },
        navigateBack = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreatePlaylistAlbumScreen(
    modifier: Modifier = Modifier,
    state: CreatePlaylistAlbumUiState,
    onEvent: (CreatePlaylistAlbumUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scroll,
                title = {
                    Text(
                        text = state.album.name.ifEmpty { stringResource(R.string.album) },
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    AppBackButton {
                        navigateBack()
                    }
                }
            )
        }
    ) {
        AnimatedContent(
            targetState = state.loadingState, label = ""
        ) { targetState ->
            when (targetState) {
                DataLoadingState.LOADING -> {

                }

                DataLoadingState.LOADED -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(it)
                        .nestedScroll(scroll.nestedScrollConnection),
                    contentPadding = PaddingValues(MaterialTheme.dimens.medium1),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
                ) {
                    items(state.albumSongs) { song ->
                        CreatePlaylistSongCard(
                            modifier = Modifier.clickable {
                                onEvent(CreatePlaylistAlbumUiEvent.OnSongClick(song.id))
                            },
                            header = state.header,
                            song = song.toUiSong()
                        )
                    }
                }

                DataLoadingState.ERROR -> CompactErrorScreen(Modifier.padding(it))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        CreatePlaylistAlbumScreen(
            state = CreatePlaylistAlbumUiState(
                loadingState = DataLoadingState.LOADED,
                albumSongs = (1..10).map {
                    CreatePlaylistPagingUiData(
                        id = it.toLong(),
                        title = "That Cool song",
                        coverImage = "",
                        artist = "That Cool Artist",
                        expandable = false,
                        isArtist = false
                    )
                }
            ),
            onEvent = {},
            navigateBack = {}
        )
    }
}