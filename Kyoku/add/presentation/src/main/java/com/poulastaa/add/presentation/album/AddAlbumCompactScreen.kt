package com.poulastaa.add.presentation.album

import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.poulastaa.add.presentation.album.components.AddAlbumSearchFilterRow
import com.poulastaa.add.presentation.album.components.AddLoadingAlbumCard
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.columnPagingLoadingLoadState
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import com.poulastaa.core.presentation.ui.components.AppLoadingSearchTopBar
import com.poulastaa.core.presentation.ui.components.AppSearchBar
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
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            if (state.loadingType == LoadingType.Content) AppSearchBar(
                modifier = Modifier.fillMaxWidth(),
                scroll = scroll,
                title = stringResource(R.string.search_album),
                isSearchOpen = true,
                query = state.query.value,
                onQueryChange = {
                    onAction(AddAlbumUiAction.OnSearchQueryChange(it))
                },
                onSearch = {
                    focusManager.clearFocus()
                },
                navigateBack = {
                    if (state.query.value.isNotEmpty()) onAction(
                        AddAlbumUiAction.OnSearchQueryChange("")
                    ) else navigateBack()
                }
            )
        }
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
                    loadingType = LoadingType.Content
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