package com.poulastaa.play.presentation.root_drawer.library

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.play.presentation.root_drawer.home.components.SuggestedArtistCard
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryFilterRow
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryHeader
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryPlaylistGird
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryPlaylistList
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryTopAppbar
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryFilterType
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryViewType

@Composable
fun LibraryCompactScreen(
    profileUrl: String,
    viewModel: LibraryViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    navigate: (ScreenEnum) -> Unit,
) {
    ObserveAsEvent(flow = viewModel.uiEvent) { event ->
        when (event) {
            is LibraryUiAction.Navigate -> {
                navigate(event.screen)
            }
        }
    }

    LibraryScreen(
        state = viewModel.state,
        profileUrl = profileUrl,
        onProfileClick = onProfileClick,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LibraryScreen(
    state: LibraryUiState,
    profileUrl: String,
    onProfileClick: () -> Unit,
    onEvent: (LibraryUiEvent) -> Unit,
) {
    val appBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            LibraryTopAppbar(
                scrollBehavior = appBarScrollBehavior,
                profileUrl = profileUrl,
                onProfileClick = onProfileClick,
                onSearchClick = {

                }
            )
        },
        modifier = Modifier.nestedScroll(appBarScrollBehavior.nestedScrollConnection)
    ) { internalPadding ->
        if (!state.canShowUi) Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(internalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary.copy(.7f),
                strokeWidth = 4.dp,
                modifier = Modifier.size(56.dp),
                strokeCap = StrokeCap.Round
            )
        } else LazyVerticalGrid(
            columns = GridCells.Fixed(state.gridSize),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(internalPadding),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
            contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
        ) {
            libraryItem(span = GridItemSpan(state.gridSize)) {
                LibraryFilterRow(
                    modifier = Modifier.fillMaxWidth(),
                    filterType = state.filterType,
                    viewType = state.viewType,
                    onClick = onEvent
                )
            }

            if (state.filterType == LibraryFilterType.ALL ||
                state.filterType == LibraryFilterType.PLAYLIST
            ) itemSection(
                gridSize = state.gridSize,
                type = state.viewType,
                data = state.data.playlist,
                header = R.string.playlist,
                onHeaderClick = {

                },
                listContent = {
                    Row(
                        modifier = Modifier
                            .height(100.dp)
                    ) {
                        LibraryPlaylistList(
                            modifier = Modifier.fillMaxSize(),
                            urls = it.urls,
                            name = it.name,
                            header = state.header,
                        )
                    }
                },
                gridContent = {
                    LibraryPlaylistGird(
                        modifier = Modifier.aspectRatio(1f),
                        urls = it.urls,
                        name = it.name,
                        header = state.header,
                    )
                }
            )

            if (state.filterType == LibraryFilterType.ALL ||
                state.filterType == LibraryFilterType.ARTIST
            ) itemSection(
                gridSize = state.gridSize,
                type = state.viewType,
                data = state.data.artist,
                header = R.string.artist,
                onHeaderClick = {

                },
                listContent = {
                    Row(
                        modifier = Modifier
                            .height(160.dp)
                    ) {
                        SuggestedArtistCard(
                            modifier = Modifier
                                .aspectRatio(1f),
                            artist = it,
                            header = state.header,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )
                    }
                },
                gridContent = {
                    SuggestedArtistCard(
                        modifier = Modifier.aspectRatio(1f),
                        artist = it,
                        header = state.header,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        maxLine = 2
                    )
                }
            )

            item(
                span = { GridItemSpan(state.gridSize) }
            ) {
                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }
}

private fun <T> LazyGridScope.itemSection(
    gridSize: Int,
    @StringRes
    header: Int,
    type: LibraryViewType,
    data: List<T>,
    onHeaderClick: () -> Unit,
    listContent: @Composable LazyGridItemScope.(T) -> Unit,
    gridContent: @Composable LazyGridItemScope.(T) -> Unit,
) {
    libraryItem(span = GridItemSpan(gridSize)) {
        LibraryHeader(
            header = stringResource(id = header),
            onAddClick = onHeaderClick
        )
    }

    items(data) {
        when (type) {
            LibraryViewType.LIST -> listContent(it)
            LibraryViewType.GRID -> gridContent(it)
        }
    }
}

private fun LazyGridScope.libraryItem(
    span: GridItemSpan,
    content: @Composable LazyGridItemScope. () -> Unit,
) {
    item(
        span = { span },
        content = content
    )
}