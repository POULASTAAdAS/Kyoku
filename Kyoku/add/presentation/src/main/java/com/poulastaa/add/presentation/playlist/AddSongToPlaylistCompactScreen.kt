package com.poulastaa.add.presentation.playlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
import com.poulastaa.core.presentation.ui.components.AppErrorScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddSongToPlaylistCompactScreen(
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
                is LoadingType.Loading -> AddSongToPlaylistLoadingTopBar(navigateBack = navigateBack)

                is LoadingType.Content -> if (horizontalPager.currentPage > state.staticData.size - 1) AddSongToPlaylistSearchTopBar(
                    query = state.query,
                    filterType = state.searchScreenFilterType,
                    onAction = onAction,
                    focusManager = focusManager,
                    navigateBack = navigateBack
                ) else AddSongToPlaylistStaticDataTopBar(
                    staticData = state.staticData,
                    currentPage = horizontalPager.currentPage,
                    navigateBack = navigateBack
                )

                else -> Unit
            }
        }
    ) {
        when (state.loadingType) {
            LoadingType.Loading -> AddSongToPlaylistLoadingContent(it) {
                LoadingSongCard(
                    Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(horizontal = MaterialTheme.dimens.medium1)
                )
            }

            is LoadingType.Error -> AppErrorScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(MaterialTheme.dimens.medium1),
                error = state.loadingType,
                navigateBack = navigateBack
            )

            LoadingType.Content -> AddSongToPlaylistCommonContent(
                horizontalPager = horizontalPager,
                values = it,
                filterType = state.searchScreenFilterType,
                onAction = onAction
            ) { pageIndex ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
                ) {
                    if (pageIndex < state.staticData.size) items(state.staticData[pageIndex].data) { item ->
                        CreatePlaylistItemCard(
                            item = item,
                            pageType = state.staticData[pageIndex].type,
                            onAction = onAction,
                            haptic = haptic
                        )
                    } else items(searchData.itemCount) { index ->
                        searchData[index]?.let { item ->
                            CreatePlaylistItemCard(
                                item = item,
                                pageType = state.staticData[pageIndex].type,
                                onAction = onAction,
                                haptic = haptic
                            )
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddSongToPlaylistCompactScreen(
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