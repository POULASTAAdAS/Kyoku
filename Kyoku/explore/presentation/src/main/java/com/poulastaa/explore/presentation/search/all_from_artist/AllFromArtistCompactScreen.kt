package com.poulastaa.explore.presentation.search.all_from_artist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppErrorScreen
import com.poulastaa.explore.presentation.search.all_from_artist.components.AllFromArtistCompactLoadingScreen
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllFromArtistCompactScreen(
    modifier: Modifier = Modifier,
    scroll: TopAppBarScrollBehavior,
    state: AllFromArtistUiState,
    song: LazyPagingItems<AllFromArtistUiItem>,
    album: LazyPagingItems<AllFromArtistUiItem>,
    onAction: (AllFromArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (state.loadingType is LoadingType.Content) {

            }
        }
    ) { paddingValues ->
        when (state.loadingType) {
            LoadingType.Loading -> AllFromArtistCompactLoadingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .padding(paddingValues)
                    .padding(MaterialTheme.dimens.medium1),
                navigateBack = navigateBack
            )

            LoadingType.Content -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scroll.nestedScrollConnection),
            ) {

            }

            is LoadingType.Error -> AppErrorScreen(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                    )
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(MaterialTheme.dimens.medium1),
                error = state.loadingType,
                navigateBack = navigateBack
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
            AllFromArtistCompactScreen(
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                state = AllFromArtistUiState(),
                song = flowOf(PagingData.from((1..10).map {
                    AllFromArtistUiItem(
                        id = it.toLong(),
                        title = "That Cool Song",
                        artist = (1..3).joinToString(",") { "Artist $it" }
                    )
                })).collectAsLazyPagingItems(),
                album = flowOf(PagingData.from((1..5).map {
                    AllFromArtistUiItem(
                        id = it.toLong(),
                        title = "That Cool Album",
                        artist = (1..3).joinToString(",") { "Artist $it" }
                    )
                })).collectAsLazyPagingItems(),
                onAction = {},
                navigateBack = {}
            )
        }
    }
}