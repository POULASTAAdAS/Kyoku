package com.poulastaa.play.presentation.root_drawer.library

import android.content.res.Configuration
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.core.presentation.ui.model.UiPrevPlaylist
import com.poulastaa.play.presentation.root_drawer.home.components.SuggestedArtistCard
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevAlbum
import com.poulastaa.play.presentation.root_drawer.library.components.FavouriteCard
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryAlbumGrid
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryAlbumList
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryFilterRow
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryHeader
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryPlaylistGird
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryPlaylistList
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryTopAppbar
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryFilterType
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryUiData
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryViewType

@Composable
fun LibraryCompactScreen(
    isExpanded: Boolean = false,
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

    LaunchedEffect(key1 = isExpanded, key2 = Unit) {
        if (isExpanded) viewModel.changeGridSizeIfExpanded()
        else viewModel.revertGridSize()
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
            contentPadding = PaddingValues(MaterialTheme.dimens.medium1),
        ) {
            fixedItem(state.gridSize) {
                LibraryFilterRow(
                    modifier = Modifier.fillMaxWidth(),
                    filterType = state.filterType,
                    viewType = state.viewType,
                    onClick = onEvent
                )
            }

            if (state.filterType == LibraryFilterType.ALL &&
                state.data.isFavouriteEntry
            ) {
                fixedItem(state.gridSize) {
                    when (state.viewType) {
                        LibraryViewType.LIST -> FavouriteCard(
                            modifier = Modifier
                                .height(100.dp),
                        ) {

                        }

                        LibraryViewType.GRID -> FavouriteCard(
                            modifier = Modifier
                                .height(100.dp)
                        ) {

                        }
                    }
                }
            }

            if (state.filterType == LibraryFilterType.ALL ||
                state.filterType == LibraryFilterType.PLAYLIST &&
                state.data.playlist.isNotEmpty()
            ) if (state.data.playlist.isNotEmpty())
                itemSection(
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
                            modifier = Modifier
                                .aspectRatio(1f),
                            urls = it.urls,
                            name = it.name,
                            header = state.header,
                        )
                    }
                )


            if (state.filterType == LibraryFilterType.ALL ||
                state.filterType == LibraryFilterType.ALBUM
            ) if (state.data.album.isNotEmpty())
                itemSection(
                    gridSize = state.gridSize,
                    type = state.viewType,
                    data = state.data.album,
                    header = R.string.album,
                    onHeaderClick = {

                    },
                    listContent = {
                        Row(
                            modifier = Modifier
                                .height(100.dp)
                        ) {
                            LibraryAlbumList(
                                modifier = Modifier.fillMaxSize(),
                                header = state.header,
                                album = it
                            )
                        }
                    },
                    gridContent = {
                        LibraryAlbumGrid(
                            modifier = Modifier.aspectRatio(1f),
                            header = state.header,
                            album = it
                        )
                    }
                )

            if (state.filterType == LibraryFilterType.ALL ||
                state.filterType == LibraryFilterType.ARTIST &&
                state.data.artist.isNotEmpty()
            ) if (state.data.artist.isNotEmpty())
                itemSection(
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
    fixedItem(gridSize) {
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

private fun LazyGridScope.fixedItem(
    span: Int,
    content: @Composable LazyGridItemScope. () -> Unit,
) {
    item(
        span = { GridItemSpan(span) },
        content = content
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 840,
    heightDp = 560
)
@Preview(
    widthDp = 840,
    heightDp = 560
)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        LibraryScreen(
            state = LibraryUiState(
                isDataLoading = false,
                viewTypeReading = false,
                filterType = LibraryFilterType.ALL,
                viewType = LibraryViewType.GRID,
                grid = 4,
                data = LibraryUiData(
                    artist = (1..10).map {
                        UiArtist(
                            name = "Artist $it"
                        )
                    },
                    playlist = (1..10).map {
                        UiPrevPlaylist(
                            id = 1,
                            name = "Playlist $it",
                            urls = emptyList()
                        )
                    },
                    isFavouriteEntry = true,
                    album = (1..10).map {
                        UiPrevAlbum(
                            id = 1,
                            name = "Album $it",
                        )
                    }
                )
            ),
            profileUrl = "",
            onProfileClick = { /*TODO*/ }
        ) {

        }
    }
}