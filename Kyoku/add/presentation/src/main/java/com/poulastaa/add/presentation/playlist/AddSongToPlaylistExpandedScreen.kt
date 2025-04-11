package com.poulastaa.add.presentation.playlist

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistCommonContent
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistLoadingContent
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistLoadingTopBar
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistSearchTopBar
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistStaticDataTopBar
import com.poulastaa.add.presentation.playlist.components.CreatePlaylistItemCard
import com.poulastaa.add.presentation.playlist.components.LoadingSongCard
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorExpandedScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddSongToPlaylistExpandedScreen(
    state: AddSongToPlaylistUiState,
    searchData: LazyPagingItems<AddToPlaylistUiItem>,
    onAction: (AddSongToPlaylistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val horizontalPager = rememberPagerState { state.staticData.size + 1 }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            when (state.loadingType) {
                LoadingType.Content -> if (horizontalPager.currentPage > state.staticData.size - 1) AddSongToPlaylistSearchTopBar(
                    query = state.query,
                    filterType = state.searchScreenFilterType,
                    onAction = onAction,
                    isExtended = true,
                    focusManager = focusManager,
                    navigateBack = navigateBack
                ) else AddSongToPlaylistStaticDataTopBar(
                    staticData = state.staticData,
                    currentPage = horizontalPager.currentPage,
                    navigateBack = navigateBack
                )

                LoadingType.Loading -> AddSongToPlaylistLoadingTopBar(
                    titleWidth = .3f,
                    navigateBack
                )

                is LoadingType.Error -> Unit
            }
        },
        content = {
            when (state.loadingType) {
                LoadingType.Loading -> AddSongToPlaylistLoadingContent(it) {
                    Row {
                        repeat(2) {
                            LoadingSongCard(
                                Modifier
                                    .weight(1f)
                                    .height(80.dp)
                                    .padding(horizontal = MaterialTheme.dimens.medium1)
                            )
                        }
                    }
                }

                is LoadingType.Error -> AppErrorExpandedScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(MaterialTheme.dimens.medium1),
                    error = state.loadingType,
                    navigateBack = navigateBack
                )

                LoadingType.Content -> AddSongToPlaylistCommonContent(
                    isExpanded = true,
                    horizontalPager = horizontalPager,
                    values = it,
                    filterType = state.searchScreenFilterType,
                    onAction = onAction
                ) { pageIndex ->
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(MaterialTheme.dimens.medium1),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
                    ) {
                        if (pageIndex < state.staticData.size) items(state.staticData[pageIndex].data) { item ->
                            CreatePlaylistItemCard(item, onAction, haptic)
                        } else items(searchData.itemCount) { index ->
                            searchData[index]?.let {
                                CreatePlaylistItemCard(it, onAction, haptic)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(
    widthDp = 1024,
    heightDp = 540
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 1280,
    heightDp = 740
)
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddSongToPlaylistExpandedScreen(
                state = AddSongToPlaylistUiState(
                    loadingType = LoadingType.Content,
                    staticData = listOf(
                        AddSongToPlaylistPageUiItem(
                            type = AddSongToPlaylistPageUiType.YOUR_FAVOURITES,
                            data = (1..10).map {
                                AddToPlaylistUiItem(
                                    title = "That Cool Song",
                                    artist = "That Cool Artist",
                                    type = AddToPlaylistItemUiType.SONG
                                )
                            }
                        ),
                        AddSongToPlaylistPageUiItem(
                            type = AddSongToPlaylistPageUiType.SUGGESTED_FOR_YOU,
                            data = (1..10).map {
                                AddToPlaylistUiItem(
                                    title = "That Cool Song",
                                    artist = "That Cool Artist",
                                    type = AddToPlaylistItemUiType.SONG
                                )
                            }
                        ),
                        AddSongToPlaylistPageUiItem(
                            type = AddSongToPlaylistPageUiType.YOU_MAY_ALSO_LIKE,
                            data = (1..10).map {
                                AddToPlaylistUiItem(
                                    title = "That Cool Song",
                                    artist = "That Cool Artist",
                                    type = AddToPlaylistItemUiType.SONG
                                )
                            }
                        )
                    )
                ),
                searchData = PREV_DATA.collectAsLazyPagingItems(),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}