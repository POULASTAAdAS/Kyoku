package com.poulastaa.add.presentation.album

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.add.presentation.album.components.AddAlbumCard
import com.poulastaa.add.presentation.album.components.AddAlbumLoadingFilterChip
import com.poulastaa.add.presentation.album.components.AddAlbumSavedButton
import com.poulastaa.add.presentation.album.components.AddAlbumSearchFilterRow
import com.poulastaa.add.presentation.album.components.AddLoadingAlbumCard
import com.poulastaa.add.presentation.album.view_album.AddAlbumFloatingActionButton
import com.poulastaa.add.presentation.album.view_album.AddAlbumViewRootScreen
import com.poulastaa.add.presentation.components.AddSearchTopBar
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.columnPagingLoadingLoadState
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CheckIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import com.poulastaa.core.presentation.ui.components.AppLoadingSearchTopBar
import com.poulastaa.core.presentation.ui.components.AppSearchBarWithDummySearch
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddAlbumCompactScreen(
    scroll: TopAppBarScrollBehavior,
    state: AddAlbumUiState,
    album: LazyPagingItems<UiAlbum>,
    onAction: (AddAlbumUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                if (state.loadingType == LoadingType.Content) AddSearchTopBar(
                    scrollBehavior = scroll,
                    label = stringResource(R.string.search_album),
                    query = state.query.value,
                    isExtended = false,
                    focusManager = LocalFocusManager.current,
                    filterTypeContent = {
                        AddAlbumSearchFilterRow(
                            searchFilterType = state.searchFilterType,
                            onAction = onAction
                        )
                    },
                    actions = {
                        AddAlbumSavedButton(
                            isEditEnabled = state.isEditEnabled,
                            isSavingAlbums = state.isSavingAlbums,
                            onAction = onAction
                        )
                    },
                    onValueChange = { onAction(AddAlbumUiAction.OnSearchQueryChange(it)) },
                    navigateBack = {
                        if (state.query.value.isNotEmpty())
                            onAction(AddAlbumUiAction.OnSearchQueryChange(""))
                        else if (state.isEditEnabled) onAction(AddAlbumUiAction.OnClearAllDialogToggle)
                        else navigateBack()
                    }
                )
            },
            floatingActionButton = {
                AddAlbumFloatingActionButton(
                    size = state.selectedAlbums.size,
                    isEditEnabled = state.isEditEnabled,
                    onAction = onAction
                )
            },
            floatingActionButtonPosition = FabPosition.Center
        ) {
            when (state.loadingType) {
                LoadingType.Loading -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = ThemModeChanger.getGradiantBackground()
                            )
                        )
                        .verticalScroll(rememberScrollState())
                        .padding(it)
                        .padding(MaterialTheme.dimens.medium1),
                ) {
                    AppLoadingSearchTopBar(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        stringResource(R.string.search_album),
                        navigateBack
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(2) {
                            AddAlbumLoadingFilterChip()

                            Spacer(Modifier.width(MaterialTheme.dimens.small2))
                        }
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                    repeat(10) {
                        AddLoadingAlbumCard()

                        Spacer(Modifier.height(MaterialTheme.dimens.small2))
                    }
                }

                is LoadingType.Error -> AppErrorScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = ThemModeChanger.getGradiantBackground()
                            )
                        )
                        .padding(it)
                        .padding(MaterialTheme.dimens.medium1),
                    error = state.loadingType,
                    navigateBack = navigateBack
                )

                LoadingType.Content -> LazyColumn(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = ThemModeChanger.getGradiantBackground()
                            )
                        )
                        .fillMaxSize()
                        .padding(it),
                    contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
                ) {
                    item {
                        AddAlbumSearchFilterRow(
                            searchFilterType = state.searchFilterType,
                            onAction = onAction
                        )
                    }

                    item {
                        Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                    }

                    items(album.itemCount) { index ->
                        album[index]?.let { album ->
                            AddAlbumCard(onAction, album)

                            Spacer(Modifier.height(MaterialTheme.dimens.small2))
                        }
                    }

                    columnPagingLoadingLoadState(
                        data = album.loadState,
                        retry = {
                            album.retry()
                        }
                    ) {
                        item {
                            Spacer(Modifier.height(MaterialTheme.dimens.small2))
                        }

                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                repeat(3) {
                                    AddLoadingAlbumCard()

                                    Spacer(Modifier.height(MaterialTheme.dimens.small2))
                                }
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = state.viewAlbumScreenState.isVisible,
            enter = fadeIn(tween(600)) + slideInVertically(tween(600), initialOffsetY = { it }),
            exit = fadeOut(tween(600)) + slideOutVertically(tween(600), targetOffsetY = { it }),
        ) {
            AddAlbumViewRootScreen(
                album = state.viewAlbumScreenState.album,
                navigateBack = {
                    onAction(AddAlbumUiAction.OnViewCancel)
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddAlbumCompactScreen(
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                state = AddAlbumUiState(
                    loadingType = LoadingType.Content,
                    selectedAlbums = (1..5).map {
                        UiAlbum(
                            name = "That Cool Album",
                            artist = "That Cool Artist",
                            releaseYear = 2025,
                            isSelected = Random.nextBoolean()
                        )
                    }
                ),
                album = flowOf(
                    PagingData.from((1..10).map {
                        UiAlbum(
                            name = "That Cool Album",
                            artist = "That Cool Artist",
                            releaseYear = 2025,
                            isSelected = Random.nextBoolean()
                        )
                    })
                ).collectAsLazyPagingItems(),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}