package com.poulastaa.add.presentation.playlist

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.add.presentation.components.AddSongToPlaylistLoadingTopBar
import com.poulastaa.add.presentation.playlist.album.AddSongToAlbumRootScreen
import com.poulastaa.add.presentation.playlist.artist.AddSongToPlaylistArtistRootScreen
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistCommonContent
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistLoadingContent
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistSearchTopBar
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistStaticDataTopBar
import com.poulastaa.add.presentation.playlist.components.CreatePlaylistItemCard
import com.poulastaa.add.presentation.playlist.components.LoadingSongCard
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddSongToPlaylistCompactScreen(
    state: AddSongToPlaylistUiState,
    searchData: LazyPagingItems<AddSongToPlaylistUiItem>,
    onAction: (AddSongToPlaylistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val horizontalPager = rememberPagerState { state.staticData.size + 1 }
    val focusManager = LocalFocusManager.current

    Box(Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                when (state.loadingType) {
                    is LoadingType.Loading -> AddSongToPlaylistLoadingTopBar(navigateBack = navigateBack)

                    is LoadingType.Content -> if (horizontalPager.currentPage > state.staticData.size - 1) AddSongToPlaylistSearchTopBar(
                        label = stringResource(R.string.search_anything),
                        query = state.query,
                        isExtended = false,
                        focusManager = focusManager,
                        onValueChange = {
                            onAction(AddSongToPlaylistUiAction.OnSearchQueryChange(it))
                        },
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
                                onAction = {
                                    onAction(
                                        AddSongToPlaylistUiAction.OnItemClick(
                                            itemId = item.id,
                                            type = item.type,
                                            pageType = state.staticData[pageIndex].type.toPageType()
                                        )
                                    )
                                },
                                haptic = haptic
                            )
                        } else items(searchData.itemCount) { index ->
                            searchData[index]?.let { item ->
                                CreatePlaylistItemCard(
                                    item = item,
                                    onAction = {
                                        focusManager.clearFocus()
                                        onAction(
                                            AddSongToPlaylistUiAction.OnItemClick(
                                                itemId = item.id,
                                                type = item.type,
                                                pageType = AddSongToPlaylistUiAction.PageType.SEARCH
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

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = state.playlistScreenState.isVisible,
            enter = fadeIn(tween(600)) + slideInVertically(tween(600), initialOffsetY = { it }),
            exit = fadeOut(tween(600)) + slideOutVertically(tween(600), targetOffsetY = { it })
        ) {

        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = state.artistScreenState.isVisible,
            enter = fadeIn(tween(600)) + slideInVertically(tween(600), initialOffsetY = { it }),
            exit = fadeOut(tween(600)) + slideOutVertically(tween(600), targetOffsetY = { it })
        ) {
            AddSongToPlaylistArtistRootScreen(
                artistId = state.artistScreenState.otherId,
                playlistId = state.playlistId,
                navigate = {
                    onAction(
                        AddSongToPlaylistUiAction.OnItemClick(
                            itemId = it,
                            type = AddToPlaylistItemUiType.ALBUM,
                            pageType = AddSongToPlaylistUiAction.PageType.SEARCH
                        )
                    )
                },
                navigateBack = {
                    onAction(
                        AddSongToPlaylistUiAction.OnOtherScreenClose(
                            AddToPlaylistItemUiType.ARTIST
                        )
                    )
                }
            )
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = state.albumScreenState.isVisible,
            enter = fadeIn(tween(600)) + slideInVertically(tween(600), initialOffsetY = { it }),
            exit = fadeOut(tween(600)) + slideOutVertically(tween(600), targetOffsetY = { it })
        ) {
            AddSongToAlbumRootScreen(
                albumId = state.albumScreenState.otherId,
                playlistId = state.playlistId,
                navigateBack = {
                    onAction(
                        AddSongToPlaylistUiAction.OnOtherScreenClose(
                            AddToPlaylistItemUiType.ALBUM
                        )
                    )
                }
            )
        }
    }

    BackHandler {
        if (state.albumScreenState.isVisible) onAction(
            AddSongToPlaylistUiAction.OnOtherScreenClose(
                AddToPlaylistItemUiType.ALBUM
            )
        ) else if (state.artistScreenState.isVisible) onAction(
            AddSongToPlaylistUiAction.OnOtherScreenClose(
                AddToPlaylistItemUiType.ARTIST
            )
        ) else if (state.playlistScreenState.isVisible) onAction(
            AddSongToPlaylistUiAction.OnOtherScreenClose(
                AddToPlaylistItemUiType.PLAYLIST
            )
        ) else navigateBack()
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
                                AddSongToPlaylistUiItem(
                                    title = "That Cool Song",
                                    artist = "That Cool Artist",
                                    type = AddToPlaylistItemUiType.SONG
                                )
                            }
                        ),
                        AddSongToPlaylistPageUiItem(
                            type = AddSongToPlaylistPageUiType.SUGGESTED_FOR_YOU,
                            data = (1..10).map {
                                AddSongToPlaylistUiItem(
                                    title = "That Cool Song",
                                    artist = "That Cool Artist",
                                    type = AddToPlaylistItemUiType.SONG
                                )
                            }
                        ),
                        AddSongToPlaylistPageUiItem(
                            type = AddSongToPlaylistPageUiType.YOU_MAY_ALSO_LIKE,
                            data = (1..10).map {
                                AddSongToPlaylistUiItem(
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