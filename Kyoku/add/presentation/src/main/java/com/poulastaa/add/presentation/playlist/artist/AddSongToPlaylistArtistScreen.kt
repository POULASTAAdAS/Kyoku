package com.poulastaa.add.presentation.playlist.artist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.add.presentation.components.AddSongToPlaylistLoadingTopBar
import com.poulastaa.add.presentation.playlist.AddSongToPlaylistUiItem
import com.poulastaa.add.presentation.playlist.AddToPlaylistItemUiType
import com.poulastaa.add.presentation.playlist.artist.components.AddSongToPlaylistArtistFilterTypes
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistSearchTopBar
import com.poulastaa.add.presentation.playlist.components.CreatePlaylistItemCard
import com.poulastaa.add.presentation.playlist.components.LoadingSongCard
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.UiPrevArtist
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddSongToPlaylistArtistScreen(
    focusManager: FocusManager = LocalFocusManager.current,
    scroll: TopAppBarScrollBehavior,
    state: AddSongToPlaylistArtistUiState,
    searchData: LazyPagingItems<AddSongToPlaylistUiItem>,
    onAction: (AddSongToPlaylistArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            when (state.loadingType) {
                LoadingType.Loading -> AddSongToPlaylistLoadingTopBar(navigateBack = navigateBack)
                LoadingType.Content -> AddSongToPlaylistSearchTopBar(
                    scrollBehavior = scroll,
                    label = "${stringResource(R.string.search)} ${state.artist.name.lowercase()}",
                    query = state.query.value,
                    isExtended = false,
                    focusManager = focusManager,
                    onValueChange = {
                        onAction(AddSongToPlaylistArtistUiAction.OnSearchQueryChange(it))
                    },
                    navigateBack = navigateBack
                )

                is LoadingType.Error -> Unit
            }
        }
    ) {
        when (state.loadingType) {
            LoadingType.Loading -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(MaterialTheme.dimens.medium1),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(10) {
                    LoadingSongCard(
                        Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.small2))
                }
            }

            is LoadingType.Error -> AppErrorScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(MaterialTheme.dimens.medium1),
                error = state.loadingType,
                navigateBack = navigateBack
            )

            LoadingType.Content -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .nestedScroll(scroll.nestedScrollConnection),
                contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
            ) {
                item {
                    AddSongToPlaylistArtistFilterTypes(state.filterType, onAction)
                }

                item {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }

                items(searchData.itemCount) { index ->
                    searchData[index]?.let { item ->
                        CreatePlaylistItemCard(
                            item = item,
                            onAction = {
                                onAction(
                                    AddSongToPlaylistArtistUiAction.OnItemClick(
                                        itemId = item.id,
                                        type = item.type,
                                    )
                                )
                            },
                            haptic = haptic
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddSongToPlaylistArtistScreen(
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                state = AddSongToPlaylistArtistUiState(
                    artist = UiPrevArtist(
                        name = "That Cool Artist"
                    ),
                    loadingType = LoadingType.Content,
                ),
                searchData = flowOf(
                    PagingData.from(
                        ((1..5).map {
                            AddSongToPlaylistUiItem(
                                id = it.toLong(),
                                title = "That Cool Song",
                                type = AddToPlaylistItemUiType.SONG
                            )
                        } + (1..5).map {
                            AddSongToPlaylistUiItem(
                                id = it.toLong(),
                                title = "That Cool Artist",
                                type = AddToPlaylistItemUiType.ALBUM
                            )
                        }).shuffled()
                    )
                ).collectAsLazyPagingItems(),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}