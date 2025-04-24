package com.poulastaa.add.presentation.playlist

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.add.presentation.components.OtherScreenUiState
import com.poulastaa.add.presentation.playlist.album.AddSongToAlbumRootScreen
import com.poulastaa.add.presentation.playlist.artist.AddSongToPlaylistArtistRootScreen
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistCommonContent
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistLoadingContent
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistLoadingTopBar
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistSearchFilterChips
import com.poulastaa.add.presentation.components.AddSearchTopBar
import com.poulastaa.add.presentation.playlist.components.AddSongToPlaylistStaticDataTopBar
import com.poulastaa.add.presentation.playlist.components.CreatePlaylistItemCard
import com.poulastaa.add.presentation.playlist.components.LoadingSongCard
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorExpandedScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddSongToPlaylistExpandedScreen(
    isExtendedSearch: Boolean = false,
    state: AddSongToPlaylistUiState,
    searchData: LazyPagingItems<AddSongToPlaylistUiItem>,
    onAction: (AddSongToPlaylistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val horizontalPager = rememberPagerState { state.staticData.size + 1 }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier.background(
            brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
        )
    ) {
        Scaffold(
            modifier = Modifier
                .animateContentSize(tween(600))
                .fillMaxHeight()
                .fillMaxWidth(
                    if (state.albumScreenState.isVisible ||
                        state.artistScreenState.isVisible ||
                        state.playlistScreenState.isVisible
                    ) .6f else 1f
                ),
            topBar = {
                when (state.loadingType) {
                    LoadingType.Content -> if (horizontalPager.currentPage > state.staticData.size - 1) AddSearchTopBar(
                        label = stringResource(R.string.search_anything),
                        query = state.query,
                        isExtended = isExtendedSearch || (state.albumScreenState.isVisible.not() &&
                                state.playlistScreenState.isVisible.not() &&
                                state.artistScreenState.isVisible.not()
                                ),
                        focusManager = focusManager,
                        onValueChange = {
                            onAction(AddSongToPlaylistUiAction.OnSearchQueryChange(it))
                        },
                        filterTypeContent = {
                            AddSongToPlaylistSearchFilterChips(
                                filterType = state.searchScreenFilterType,
                                onAction = onAction
                            )
                        },
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

                    LoadingType.Content -> Column(
                        modifier = Modifier.animateContentSize(tween(600))
                    ) {
                        if (isExtendedSearch.not() &&
                            horizontalPager.currentPage > state.staticData.size - 1 && (
                                    state.albumScreenState.isVisible ||
                                            state.playlistScreenState.isVisible ||
                                            state.artistScreenState.isVisible
                                    )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = it.calculateTopPadding(),
                                        start = MaterialTheme.dimens.medium1
                                    )
                            ) {
                                AddSongToPlaylistSearchFilterChips(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateContentSize(tween(600)),
                                    filterType = state.searchScreenFilterType,
                                    onAction = onAction
                                )
                            }
                        }

                        AddSongToPlaylistCommonContent(
                            isExpanded = true,
                            horizontalPager = horizontalPager,
                            values = PaddingValues(
                                top = if (isExtendedSearch.not() &&
                                    horizontalPager.currentPage > state.staticData.size - 1 && (
                                            state.albumScreenState.isVisible ||
                                                    state.playlistScreenState.isVisible ||
                                                    state.artistScreenState.isVisible
                                            )
                                ) MaterialTheme.dimens.small2 else it.calculateTopPadding(),
                                start = it.calculateStartPadding(LayoutDirection.Ltr),
                                end = it.calculateEndPadding(LayoutDirection.Rtl),
                                bottom = it.calculateBottomPadding()
                            ),
                            filterType = state.searchScreenFilterType,
                            onAction = onAction
                        ) { pageIndex ->
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .animateContentSize(tween(600)),
                                contentPadding = PaddingValues(MaterialTheme.dimens.medium1),
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
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
            }
        )

        Box(Modifier.fillMaxSize()) {
            this@Row.AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visible = state.playlistScreenState.isVisible,
                enter = fadeIn(tween(600)) + slideInHorizontally(
                    tween(600),
                    initialOffsetX = { it }),
                exit = fadeOut(tween(600)) + slideOutHorizontally(
                    tween(600),
                    targetOffsetX = { it })
            ) {

            }

            this@Row.AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visible = state.artistScreenState.isVisible,
                enter = fadeIn(tween(600)) + slideInHorizontally(
                    tween(600),
                    initialOffsetX = { it }),
                exit = fadeOut(tween(600)) + slideOutHorizontally(
                    tween(600),
                    targetOffsetX = { it })
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

            this@Row.AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visible = state.albumScreenState.isVisible,
                enter = fadeIn(tween(600)) + slideInHorizontally(
                    tween(600),
                    initialOffsetX = { it }
                ),
                exit = fadeOut(tween(600)) + slideOutHorizontally(
                    tween(600),
                    targetOffsetX = { it }
                )
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
    var open by remember { mutableStateOf(false) }

    AppThem {
        Surface {
            AddSongToPlaylistExpandedScreen(
                state = AddSongToPlaylistUiState(
                    loadingType = LoadingType.Content,
                    albumScreenState = OtherScreenUiState(
                        isVisible = open
                    ),
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
                onAction = {
                    open = !open
                },
                navigateBack = {}
            )
        }
    }
}