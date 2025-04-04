package com.poulastaa.explore.presentation.search.artist

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
import com.poulastaa.explore.presentation.components.ExploreCompactLoadingScreen
import com.poulastaa.explore.presentation.components.ExploreScreenExtendedWrapper
import com.poulastaa.explore.presentation.model.ExploreUiItem
import com.poulastaa.explore.presentation.search.artist.components.ExploreArtistCard
import com.poulastaa.explore.presentation.search.artist.components.ExploreArtistFilterType
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExploreArtistScreen(
    modifier: Modifier = Modifier,
    searchBarWidth: Float = 1f,
    itemCount: Int,
    scroll: TopAppBarScrollBehavior,
    state: ExploreArtistUiState,
    artist: LazyPagingItems<ExploreUiItem>,
    onAction: (ExploreArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    ExploreScreenExtendedWrapper(
        modifier = modifier,
        scroll = scroll,
        loadingType = state.loadingType,
        isSearchOpen = state.isSearchOpen,
        title = stringResource(R.string.artist),
        query = state.query.value,
        onQueryChange = {
            onAction(ExploreArtistUiAction.OnSearchQueryChange(it))
        },
        onToggleSearch = {
            onAction(ExploreArtistUiAction.OnSearchToggle)
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
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                    )
                    .animateContentSize(tween(400))
                    .padding(it)
                    .nestedScroll(scroll.nestedScrollConnection),
                columns = GridCells.Fixed(itemCount),
                contentPadding = PaddingValues(MaterialTheme.dimens.medium1),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1),
            ) {
                if (state.isSearchOpen.not()) item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DummySearch(
                            modifier = Modifier.fillMaxSize(searchBarWidth),
                            isSearchOpen = state.isSearchOpen,
                            onToggleSearch = { onAction(ExploreArtistUiAction.OnSearchToggle) },
                        )
                    }
                }

                item(span = { GridItemSpan(maxCurrentLineSpan) }) {}

                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    ExploreArtistFilterType(state.filterType, onAction)
                }

                item(span = { GridItemSpan(maxCurrentLineSpan) }) {}

                items(artist.itemCount) { index ->
                    artist[index]?.let { item ->
                        ExploreArtistCard(
                            modifier = Modifier
                                .aspectRatio(1f),
                            artist = item,
                            onAction = onAction
                        )
                    }
                }
            }
        },
        navigateBack = {
            when {
                state.isSearchOpen && state.query.value.isNotEmpty() ->
                    onAction(ExploreArtistUiAction.OnSearchQueryChange(""))

                state.isSearchOpen -> onAction(ExploreArtistUiAction.OnSearchToggle)
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
    var filterType by remember { mutableStateOf(SEARCH_ARTIST_FILTER_TYPE.ALL) }

    AppThem {
        Surface {
            ExploreArtistScreen(
                modifier = Modifier.fillMaxWidth(),
                itemCount = 3,
                state = ExploreArtistUiState(
                    loadingType = LoadingType.Content,
                    filterType = filterType,
                    isSearchOpen = search
                ),
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                artist = flowOf(PagingData.from((1..20).map {
                    ExploreUiItem(
                        id = it.toLong(),
                        title = "That Cool Artist",
                        releaseYear = 2025
                    )
                })).collectAsLazyPagingItems(),
                onAction = {
                    if (it is ExploreArtistUiAction.OnSearchToggle) search = !search
                },
                navigateBack = {}
            )
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
private fun PreviewExtended() {
    var search by remember { mutableStateOf(false) }
    var filterType by remember { mutableStateOf(SEARCH_ARTIST_FILTER_TYPE.ALL) }

    AppThem {
        Surface {
            ExploreArtistScreen(
                modifier = Modifier.fillMaxWidth(.7f),
                searchBarWidth = .7f,
                itemCount = 6,
                state = ExploreArtistUiState(
                    loadingType = LoadingType.Content,
                    filterType = filterType,
                    isSearchOpen = search
                ),
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                artist = flowOf(PagingData.from((1..20).map {
                    ExploreUiItem(
                        id = it.toLong(),
                        title = "That Cool Artist",
                        releaseYear = 2025
                    )
                })).collectAsLazyPagingItems(),
                onAction = {
                    if (it is ExploreArtistUiAction.OnSearchToggle) search = !search
                },
                navigateBack = {}
            )
        }
    }
}