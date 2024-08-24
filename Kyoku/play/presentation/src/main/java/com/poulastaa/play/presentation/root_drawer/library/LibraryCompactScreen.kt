package com.poulastaa.play.presentation.root_drawer.library

import android.content.res.Configuration
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.domain.model.PinnedType
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
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryItemBottomSheet
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryPlaylistGird
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryPlaylistList
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryToast
import com.poulastaa.play.presentation.root_drawer.library.components.LibraryTopAppbar
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryFilterType
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryUiData
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryViewType
import kotlinx.coroutines.launch

@Composable
fun LibraryCompactScreen(
    isExpanded: Boolean = false,
    profileUrl: String,
    viewModel: LibraryViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    navigate: (LibraryOtherScreen) -> Unit,
) {
    val context = LocalContext.current

    ObserveAsEvent(flow = viewModel.uiEvent) { event ->
        when (event) {
            is LibraryUiAction.Navigate -> navigate(event.screen)

            is LibraryUiAction.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun LibraryScreen(
    state: LibraryUiState,
    profileUrl: String,
    onProfileClick: () -> Unit,
    onEvent: (LibraryUiEvent) -> Unit,
) {
    val appBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val libraryBottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            LibraryTopAppbar(
                scrollBehavior = appBarScrollBehavior,
                profileUrl = profileUrl,
                onProfileClick = onProfileClick,
                onSearchClick = {
                    onEvent(LibraryUiEvent.OnSearchClick)
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
        } else Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            LibraryToast(data = state.toast, internalPadding)

            LazyVerticalGrid(
                columns = GridCells.Fixed(state.gridSize),
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
                    .then(
                        if (state.toast.isVisible) Modifier
                            .padding(
                                start = internalPadding.calculateStartPadding(LayoutDirection.Ltr),
                                bottom = internalPadding.calculateBottomPadding(),
                                end = internalPadding.calculateStartPadding(LayoutDirection.Rtl)
                            )
                        else Modifier.padding(internalPadding)
                    ),
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
                    (state.data.pinned.isNotEmpty() ||
                            state.data.isFevPinned)
                ) {
                    itemSection(
                        gridSize = state.gridSize,
                        type = state.viewType,
                        data = state.data.pinned,
                        header = R.string.pinned,
                        onHeaderClick = {
                            onEvent(LibraryUiEvent.OnClick.PinnedHeader)
                        },
                        listContent = {
                            Row(
                                modifier = Modifier
                                    .height(100.dp)
                            ) {
                                when (it.pinnedType) {
                                    PinnedType.PLAYLIST -> {
                                        LibraryPlaylistList(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(MaterialTheme.shapes.small)
                                                .combinedClickable(
                                                    onClick = {
                                                        onEvent(LibraryUiEvent.OnClick.Playlist(it.id))
                                                    },
                                                    onLongClick = {
                                                        onEvent(
                                                            LibraryUiEvent.OnItemLongClick(
                                                                id = it.id,
                                                                type = LibraryBottomSheetLongClickType.PLAYLIST
                                                            )
                                                        )

                                                        haptic.performHapticFeedback(
                                                            HapticFeedbackType.LongPress
                                                        )
                                                    }
                                                ),
                                            urls = it.urls,
                                            name = it.name,
                                            header = state.header,
                                        )
                                    }

                                    PinnedType.ARTIST -> {
                                        SuggestedArtistCard(
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                                .clip(MaterialTheme.shapes.small)
                                                .combinedClickable(
                                                    onClick = {
                                                        onEvent(LibraryUiEvent.OnClick.Artist(it.id))
                                                    },
                                                    onLongClick = {
                                                        onEvent(
                                                            LibraryUiEvent.OnItemLongClick(
                                                                id = it.id,
                                                                type = LibraryBottomSheetLongClickType.ARTIST
                                                            )
                                                        )

                                                        haptic.performHapticFeedback(
                                                            HapticFeedbackType.LongPress
                                                        )
                                                    }
                                                ),
                                            artist = it.toUiArtist(),
                                            header = state.header,
                                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                                        )
                                    }

                                    PinnedType.ALBUM -> {
                                        LibraryAlbumList(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(MaterialTheme.shapes.small)
                                                .combinedClickable(
                                                    onClick = {
                                                        onEvent(LibraryUiEvent.OnClick.Album(it.id))
                                                    },
                                                    onLongClick = {
                                                        onEvent(
                                                            LibraryUiEvent.OnItemLongClick(
                                                                id = it.id,
                                                                type = LibraryBottomSheetLongClickType.ALBUM
                                                            )
                                                        )

                                                        haptic.performHapticFeedback(
                                                            HapticFeedbackType.LongPress
                                                        )
                                                    }
                                                ),
                                            header = state.header,
                                            album = it.toUiAlbum()
                                        )
                                    }
                                }
                            }
                        },
                        gridContent = {
                            when (it.pinnedType) {
                                PinnedType.PLAYLIST -> {
                                    LibraryPlaylistGird(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .clip(MaterialTheme.shapes.small)
                                            .combinedClickable(
                                                onClick = {
                                                    onEvent(LibraryUiEvent.OnClick.Playlist(it.id))
                                                },
                                                onLongClick = {
                                                    onEvent(
                                                        LibraryUiEvent.OnItemLongClick(
                                                            id = it.id,
                                                            type = LibraryBottomSheetLongClickType.PLAYLIST
                                                        )
                                                    )

                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                }
                                            ),
                                        urls = it.urls,
                                        name = it.name,
                                        header = state.header,
                                    )
                                }

                                PinnedType.ARTIST -> {
                                    SuggestedArtistCard(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .clip(MaterialTheme.shapes.small)
                                            .combinedClickable(
                                                onClick = {
                                                    onEvent(LibraryUiEvent.OnClick.Artist(it.id))
                                                },
                                                onLongClick = {
                                                    onEvent(
                                                        LibraryUiEvent.OnItemLongClick(
                                                            id = it.id,
                                                            type = LibraryBottomSheetLongClickType.ARTIST
                                                        )
                                                    )

                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                }
                                            ),
                                        artist = it.toUiArtist(),
                                        header = state.header,
                                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                        maxLine = 2
                                    )
                                }

                                PinnedType.ALBUM -> {
                                    LibraryAlbumGrid(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .clip(MaterialTheme.shapes.small)
                                            .combinedClickable(
                                                onClick = {
                                                    onEvent(LibraryUiEvent.OnClick.Album(it.id))
                                                },
                                                onLongClick = {
                                                    onEvent(
                                                        LibraryUiEvent.OnItemLongClick(
                                                            id = it.id,
                                                            type = LibraryBottomSheetLongClickType.ALBUM
                                                        )
                                                    )

                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                }
                                            ),
                                        header = state.header,
                                        album = it.toUiAlbum()
                                    )
                                }
                            }
                        }
                    )

                    if (state.data.isFevPinned) item(span = { GridItemSpan(state.gridSize) }) {
                        FavouriteCard(
                            modifier = Modifier
                                .height(100.dp)
                                .combinedClickable(
                                    onClick = {
                                        onEvent(LibraryUiEvent.OnClick.Favourite)
                                    },
                                    onLongClick = {
                                        onEvent(
                                            LibraryUiEvent.OnItemLongClick(
                                                id = -1,
                                                type = LibraryBottomSheetLongClickType.FAVOURITE
                                            )
                                        )

                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                )
                        )
                    }

                    item(
                        span = { GridItemSpan(state.gridSize) }
                    ) {
                        HorizontalDivider(
                            thickness = 2.0.dp
                        )
                    }
                }

                if (state.filterType == LibraryFilterType.ALL &&
                    state.data.isFavouriteEntry
                ) {
                    fixedItem(state.gridSize) {
                        when (state.viewType) {
                            LibraryViewType.LIST -> FavouriteCard(
                                modifier = Modifier
                                    .height(100.dp)
                                    .combinedClickable(
                                        onClick = {
                                            onEvent(LibraryUiEvent.OnClick.Favourite)
                                        },
                                        onLongClick = {
                                            onEvent(
                                                LibraryUiEvent.OnItemLongClick(
                                                    id = -1,
                                                    type = LibraryBottomSheetLongClickType.FAVOURITE
                                                )
                                            )

                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        }
                                    ),
                            )

                            LibraryViewType.GRID -> FavouriteCard(
                                modifier = Modifier
                                    .height(100.dp)
                                    .combinedClickable(
                                        onClick = {
                                            onEvent(LibraryUiEvent.OnClick.Favourite)
                                        },
                                        onLongClick = {
                                            onEvent(
                                                LibraryUiEvent.OnItemLongClick(
                                                    id = -1,
                                                    type = LibraryBottomSheetLongClickType.FAVOURITE
                                                )
                                            )

                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        }
                                    )
                            )
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
                            onEvent(LibraryUiEvent.OnClick.PlaylistHeader)
                        },
                        listContent = {
                            Row(
                                modifier = Modifier
                                    .height(100.dp)
                            ) {
                                LibraryPlaylistList(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(MaterialTheme.shapes.small)
                                        .combinedClickable(
                                            onClick = {
                                                onEvent(LibraryUiEvent.OnClick.Playlist(it.id))
                                            },
                                            onLongClick = {
                                                onEvent(
                                                    LibraryUiEvent.OnItemLongClick(
                                                        id = it.id,
                                                        type = LibraryBottomSheetLongClickType.PLAYLIST
                                                    )
                                                )

                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            }
                                        ),
                                    urls = it.urls,
                                    name = it.name,
                                    header = state.header,
                                )
                            }
                        },
                        gridContent = {
                            LibraryPlaylistGird(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(MaterialTheme.shapes.small)
                                    .combinedClickable(
                                        onClick = {
                                            onEvent(LibraryUiEvent.OnClick.Playlist(it.id))
                                        },
                                        onLongClick = {
                                            onEvent(
                                                LibraryUiEvent.OnItemLongClick(
                                                    id = it.id,
                                                    type = LibraryBottomSheetLongClickType.PLAYLIST
                                                )
                                            )

                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        }
                                    ),
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
                            onEvent(LibraryUiEvent.OnClick.AlbumHeader)
                        },
                        listContent = {
                            Row(
                                modifier = Modifier
                                    .height(100.dp)
                            ) {
                                LibraryAlbumList(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(MaterialTheme.shapes.small)
                                        .combinedClickable(
                                            onClick = {
                                                onEvent(LibraryUiEvent.OnClick.Album(it.id))
                                            },
                                            onLongClick = {
                                                onEvent(
                                                    LibraryUiEvent.OnItemLongClick(
                                                        id = it.id,
                                                        type = LibraryBottomSheetLongClickType.ALBUM
                                                    )
                                                )

                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            }
                                        ),
                                    header = state.header,
                                    album = it
                                )
                            }
                        },
                        gridContent = {
                            LibraryAlbumGrid(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(MaterialTheme.shapes.small)
                                    .combinedClickable(
                                        onClick = {
                                            onEvent(LibraryUiEvent.OnClick.Album(it.id))
                                        },
                                        onLongClick = {
                                            onEvent(
                                                LibraryUiEvent.OnItemLongClick(
                                                    id = it.id,
                                                    type = LibraryBottomSheetLongClickType.ALBUM
                                                )
                                            )

                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        }
                                    ),
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
                            onEvent(LibraryUiEvent.OnClick.ArtistHeader)
                        },
                        listContent = {
                            Row(
                                modifier = Modifier
                                    .height(160.dp)
                            ) {
                                SuggestedArtistCard(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clip(MaterialTheme.shapes.small)
                                        .combinedClickable(
                                            onClick = {
                                                onEvent(LibraryUiEvent.OnClick.Artist(it.id))
                                            },
                                            onLongClick = {
                                                onEvent(
                                                    LibraryUiEvent.OnItemLongClick(
                                                        id = it.id,
                                                        type = LibraryBottomSheetLongClickType.ARTIST
                                                    )
                                                )

                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            }
                                        ),
                                    artist = it,
                                    header = state.header,
                                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                                )
                            }
                        },
                        gridContent = {
                            SuggestedArtistCard(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(MaterialTheme.shapes.small)
                                    .combinedClickable(
                                        onClick = {
                                            onEvent(LibraryUiEvent.OnClick.Artist(it.id))
                                        },
                                        onLongClick = {
                                            onEvent(
                                                LibraryUiEvent.OnItemLongClick(
                                                    id = it.id,
                                                    type = LibraryBottomSheetLongClickType.ARTIST
                                                )
                                            )

                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        }
                                    ),
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

    LaunchedEffect(key1 = state.libraryBottomSheet.isOpen) {
        if (state.libraryBottomSheet.isOpen) libraryBottomSheetState.show()
    }

    if (state.libraryBottomSheet.isOpen) LibraryItemBottomSheet(
        sheetState = libraryBottomSheetState,
        header = state.header,
        state = state.libraryBottomSheet,
        onEvent = onEvent
    ) {
        coroutineScope.launch {
            libraryBottomSheetState.hide()
        }.invokeOnCompletion {
            onEvent(LibraryUiEvent.OnItemBottomSheetCancel)
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