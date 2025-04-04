package com.poulastaa.explore.presentation.search.album

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import com.poulastaa.explore.presentation.components.DummySearch
import com.poulastaa.explore.presentation.components.ExploreLoadingTopBar
import com.poulastaa.explore.presentation.components.ExploreScreenExtendedWrapper
import com.poulastaa.explore.presentation.components.LoadingSongCard
import com.poulastaa.explore.presentation.model.ExploreUiItem
import com.poulastaa.explore.presentation.search.album.components.ExploreAlbumCard
import com.poulastaa.explore.presentation.search.album.components.ExploreAlbumFilterTypes
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExploreAlbumExpandedScreen(
    modifier: Modifier = Modifier,
    scroll: TopAppBarScrollBehavior,
    state: ExploreAlbumUiState,
    album: LazyPagingItems<ExploreUiItem>,
    onAction: (ExploreAlbumUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    ExploreScreenExtendedWrapper(
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
        loadingContent = { paddingValues ->
            ExtendedLoadingContent(paddingValues, navigateBack)
        },
        errorContent = { paddingValues, error ->
            AppErrorScreen(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(ThemModeChanger.getGradiantBackground())
                    )
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(MaterialTheme.dimens.medium1),
                error = error,
                navigateBack = navigateBack
            )
        },
        content = {
            LazyVerticalGrid(
                modifier = Modifier
                    .animateContentSize(tween(400))
                    .padding(it)
                    .nestedScroll(scroll.nestedScrollConnection),
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(MaterialTheme.dimens.medium1),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
            ) {
                if (state.isSearchOpen.not()) item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DummySearch(
                            modifier = Modifier
                                .fillMaxWidth(.7f)
                                .fillMaxHeight(),
                            isSearchOpen = state.isSearchOpen,
                            onToggleSearch = { onAction(ExploreAlbumUiAction.OnSearchToggle) },
                        )
                    }
                }

                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    ExploreAlbumFilterTypes(state.filterType, onAction)
                }

                items(album.itemCount) { index ->
                    album[index]?.let { album ->
                        ExploreAlbumCard(
                            album = album,
                            filterType = state.filterType,
                            onAction = onAction,
                        )
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

@Composable
private fun ExtendedLoadingContent(
    values: PaddingValues,
    navigateBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    ThemModeChanger.getGradiantBackground()
                )
            )
            .padding(values)
            .padding(horizontal = MaterialTheme.dimens.medium1)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(MaterialTheme.dimens.medium1))
        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        ExploreLoadingTopBar(
            Modifier
                .fillMaxWidth(.7f)
                .height(56.dp),
            stringResource(R.string.album),
            navigateBack
        )

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        repeat(15) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(3) {
                    LoadingSongCard(
                        Modifier
                            .weight(1f)
                            .height(80.dp)
                    )

                    Spacer(Modifier.width(MaterialTheme.dimens.small2))
                }
            }

            Spacer(Modifier.height(MaterialTheme.dimens.small2))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
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
    var search by remember { mutableStateOf(false) }
    var filterType by remember { mutableStateOf(SEARCH_ALBUM_FILTER_TYPE.RELEASE_YEAR) }

    AppThem {
        Surface {
            ExploreAlbumExpandedScreen(
                modifier = Modifier.fillMaxWidth(.7f),
                state = ExploreAlbumUiState(
                    loadingType = LoadingType.Content,
                    filterType = filterType,
                    isSearchOpen = search
                ),
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                album = flowOf(PagingData.from((1..50).map {
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