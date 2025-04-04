package com.poulastaa.explore.presentation.search.artist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import com.poulastaa.explore.presentation.components.ExploreCompactLoadingScreen
import com.poulastaa.explore.presentation.components.ExploreScreenWrapper
import com.poulastaa.explore.presentation.model.ExploreUiItem
import com.poulastaa.explore.presentation.search.artist.components.ExploreArtistFilterType
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExploreArtistCompactScreen(
    modifier: Modifier = Modifier,
    scroll: TopAppBarScrollBehavior,
    state: ExploreArtistUiState,
    artist: LazyPagingItems<ExploreUiItem>,
    onAction: (ExploreArtistUiAction) -> Unit,
    navigateBack: () -> Unit
) {
    ExploreScreenWrapper(
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
            item {
                ExploreArtistFilterType(state.filterType, onAction)
            }

            items(artist.itemCount) { index ->
                artist[index]?.let {

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
            ExploreArtistCompactScreen(
                modifier = Modifier.fillMaxWidth(),
                state = ExploreArtistUiState(
                    loadingType = LoadingType.Content,
                    filterType = filterType,
                    isSearchOpen = search
                ),
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                artist = flowOf(PagingData.from((1..10).map {
                    ExploreUiItem(
                        id = it.toLong(),
                        title = "That Cool Album",
                        artist = "That Cool Artist",
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