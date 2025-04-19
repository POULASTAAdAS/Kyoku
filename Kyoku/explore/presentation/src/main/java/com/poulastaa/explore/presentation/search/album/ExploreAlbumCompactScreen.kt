package com.poulastaa.explore.presentation.search.album

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.columnPagingLoadingLoadState
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import com.poulastaa.explore.presentation.components.ExploreCompactLoadingScreen
import com.poulastaa.explore.presentation.components.ExploreScreenWrapper
import com.poulastaa.explore.presentation.components.LoadingSongCard
import com.poulastaa.explore.presentation.model.ExploreUiItem
import com.poulastaa.explore.presentation.search.album.components.ExploreAlbumCard
import com.poulastaa.explore.presentation.search.album.components.ExploreAlbumFilterTypes
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExploreAlbumCompactScreen(
    modifier: Modifier = Modifier,
    scroll: TopAppBarScrollBehavior,
    state: ExploreAlbumUiState,
    album: LazyPagingItems<ExploreUiItem>,
    onAction: (ExploreAlbumUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    ExploreScreenWrapper(
        modifier = modifier,
        scroll = scroll,
        loadingType = state.loadingType,
        isSearchOpen = state.isSearchOpen,
        title = stringResource(R.string.album),
        query = state.query.value,
        onQueryChange = {
            onAction(ExploreAlbumUiAction.OnSearchQueryChange(it))
        },
        onToggleSearch = {
            onAction(ExploreAlbumUiAction.OnSearchToggle)
        },
        loadingContent = {
            ExploreCompactLoadingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .padding(it)
                    .padding(horizontal = MaterialTheme.dimens.medium1),
                title = stringResource(R.string.that_cool_album),
                navigateBack = navigateBack
            )
        },
        errorContent = { paddingValues, error ->
            AppErrorScreen(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                    )
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(MaterialTheme.dimens.medium1),
                error = error,
                navigateBack = navigateBack
            )
        },
        content = {
            item {
                ExploreAlbumFilterTypes(state.filterType, onAction)
            }

            item {
                Spacer(Modifier.height(MaterialTheme.dimens.medium1))
            }

            items(album.itemCount) { index ->
                album[index]?.let { album ->
                    Spacer(Modifier.height(MaterialTheme.dimens.small1))

                    ExploreAlbumCard(
                        album = album,
                        filterType = state.filterType,
                        onAction = onAction,
                    )
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
                            LoadingSongCard(
                                Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                            )

                            Spacer(Modifier.height(MaterialTheme.dimens.small2))
                        }
                    }
                }
            }
        },
        navigateBack = {
            when {
                state.isSearchOpen && state.query.value.isNotEmpty() ->
                    onAction(ExploreAlbumUiAction.OnSearchQueryChange(""))

                state.isSearchOpen -> onAction(ExploreAlbumUiAction.OnSearchToggle)
                else -> navigateBack()
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    var search by remember { mutableStateOf(false) }
    var filterType by remember { mutableStateOf(SEARCH_ALBUM_FILTER_TYPE.RELEASE_YEAR) }

    AppThem {
        Surface {
            ExploreAlbumCompactScreen(
                modifier = Modifier.fillMaxWidth(),
                state = ExploreAlbumUiState(
                    loadingType = LoadingType.Content,
                    filterType = filterType,
                    isSearchOpen = search
                ),
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                album = flowOf(PagingData.from((1..10).map {
                    ExploreUiItem(
                        id = it.toLong(),
                        title = "That Cool Album",
                        artist = "That Cool Artist",
                        releaseYear = 2025
                    )
                })).collectAsLazyPagingItems(),
                onAction = {
                    if (it is ExploreAlbumUiAction.OnFilterTypeChange) filterType = it.type
                    if (it is ExploreAlbumUiAction.OnSearchToggle) search = !search
                },
                navigateBack = {}
            )
        }
    }
}