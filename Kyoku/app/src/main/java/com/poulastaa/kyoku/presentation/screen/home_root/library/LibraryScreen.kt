package com.poulastaa.kyoku.presentation.screen.home_root.library

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.UiEvent
import com.poulastaa.kyoku.data.model.screens.library.LibraryUiEvent
import com.poulastaa.kyoku.presentation.common.ItemDeleteDialog
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomToast
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.LibraryScreenBottomSheet
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.album
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.artist
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.favourite
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.filterChips
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.headLineSeparator
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.largeSpace
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.libraryScreenItemAlbum
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.libraryScreenItemArtist
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.libraryScreenItemPlaylist
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.playlist
import com.poulastaa.kyoku.ui.theme.dimens
import com.poulastaa.kyoku.utils.Constants.PLAYER_PADDING
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    sheetState: SheetState = rememberModalBottomSheetState(),
    viewModel: LibraryViewModel = hiltViewModel(),
    isSmallPhone: Boolean,
    isCookie: Boolean,
    headerValue: String,
    context: Context,
    scope: CoroutineScope = rememberCoroutineScope(),
    paddingValues: PaddingValues,
    navigate: (UiEvent) -> Unit
) {
    LaunchedEffect(key1 = viewModel.state.isInternetAvailable) {
        viewModel.loadData()
    }

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    navigate.invoke(event)
                }

                is UiEvent.NavigateWithData -> {
                    navigate.invoke(event)
                }

                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> Unit
            }
        }
    }

    if (viewModel.state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(
                if (viewModel.state.isGrid) viewModel.state.maxGridSize
                else viewModel.state.minGridSize
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
            contentPadding = PaddingValues(
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1,
                bottom = MaterialTheme.dimens.medium2
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
        ) {
            // filter chips
            filterChips(
                span = GridItemSpan(
                    if (viewModel.state.isGrid) viewModel.state.maxGridSize
                    else viewModel.state.minGridSize
                ),
                isGrid = viewModel.state.isGrid,
                filterChip = viewModel.state.filterChip,
                filterByAlbum = viewModel::onEvent,
                filterByArtist = viewModel::onEvent,
                filterByPlaylist = viewModel::onEvent,
                sortOrderClick = viewModel::onEvent
            )

            // no internet toast
            item(
                span = {
                    GridItemSpan(
                        if (viewModel.state.isGrid) viewModel.state.maxGridSize
                        else viewModel.state.minGridSize
                    )
                }
            ) {
                AnimatedVisibility(
                    visible = viewModel.state.isInternetError,
                    enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + expandIn(),
                    exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + shrinkOut()
                ) {
                    val temp = remember {
                        viewModel.state.isInternetError
                    }

                    if (temp) {
                        CustomToast(
                            message = viewModel.state.errorMessage,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // filtered items
            if (viewModel.state.filterChip.isPlaylist &&
                viewModel.state.data.all.playlist.isNotEmpty()
            ) libraryScreenItemPlaylist(
                playlistPrev = viewModel.state.data.all.playlist,
                sizeIfGrid = if (isSmallPhone) 120.dp else 130.dp,
                sizeIfList = 80.dp,
                isCookie = isCookie,
                headerValue = headerValue,
                isGrid = viewModel.state.isGrid,
                scope = scope,
                onLongClick = {},
                onClick = viewModel::onEvent
            )

            if (viewModel.state.filterChip.isAlbum &&
                viewModel.state.data.all.album.isNotEmpty()
            ) libraryScreenItemAlbum(
                albums = viewModel.state.data.all.album,
                sizeIfGrid = 90.dp,
                sizeIfList = 80.dp,
                isCookie = isCookie,
                headerValue = headerValue,
                isGrid = viewModel.state.isGrid,
                scope = scope,
                onLongClick = {},
                onClick = viewModel::onEvent
            )

            if (viewModel.state.filterChip.isArtist &&
                viewModel.state.data.all.artist.isNotEmpty()
            ) libraryScreenItemArtist(
                artistPrev = viewModel.state.data.all.artist,
                sizeIfGrid = 90.dp,
                sizeIfList = 80.dp,
                isCookie = isCookie,
                headerValue = headerValue,
                isGrid = viewModel.state.isGrid,
                scope = scope,
                onLongClick = {},
                onClick = viewModel::onEvent
            )

            if (!viewModel.state.filterChip.isAlbum &&
                !viewModel.state.filterChip.isArtist &&
                !viewModel.state.filterChip.isPlaylist
            ) {
                // pinned heading
                item(
                    span = {
                        GridItemSpan(
                            if (viewModel.state.isGrid) viewModel.state.maxGridSize
                            else viewModel.state.minGridSize
                        )
                    }
                ) {
                    Text(
                        text = "Pinned",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Black,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize
                    )
                }

                headLineSeparator(viewModel.state.isGrid)

                // pinned items if empty
                if (
                    !viewModel.state.data.pinned.isFavourite &&
                    viewModel.state.data.pinned.playlist.isEmpty() &&
                    viewModel.state.data.pinned.artist.isEmpty()
                ) {
                    item(
                        span = {
                            GridItemSpan(
                                if (viewModel.state.isGrid) viewModel.state.maxGridSize
                                else viewModel.state.minGridSize
                            )
                        }
                    ) {
                        Text(
                            text = "Long press to pin",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Light,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }


                // pinned items
                if (viewModel.state.data.pinned.isFavourite)
                    favourite(
                        span = GridItemSpan(
                            if (viewModel.state.isGrid) viewModel.state.maxGridSize
                            else viewModel.state.minGridSize
                        ),
                        scope = scope,
                        onLongClick = viewModel::onEvent,
                        onClick = viewModel::onEvent
                    )

                if (viewModel.state.data.pinned.playlist.isNotEmpty())
                    libraryScreenItemPlaylist(
                        playlistPrev = viewModel.state.data.pinned.playlist,
                        sizeIfGrid = 90.dp,
                        sizeIfList = 80.dp,
                        isCookie = isCookie,
                        headerValue = headerValue,
                        isGrid = viewModel.state.isGrid,
                        scope = scope,
                        onLongClick = viewModel::onEvent,
                        onClick = viewModel::onEvent
                    )

                if (viewModel.state.data.pinned.album.isNotEmpty())
                    libraryScreenItemAlbum(
                        albums = viewModel.state.data.pinned.album,
                        sizeIfGrid = 90.dp,
                        sizeIfList = 80.dp,
                        isCookie = isCookie,
                        headerValue = headerValue,
                        isGrid = viewModel.state.isGrid,
                        scope = scope,
                        onLongClick = viewModel::onEvent,
                        onClick = viewModel::onEvent
                    )

                if (viewModel.state.data.pinned.artist.isNotEmpty())
                    libraryScreenItemArtist(
                        artistPrev = viewModel.state.data.pinned.artist,
                        sizeIfGrid = 90.dp,
                        sizeIfList = 80.dp,
                        isCookie = isCookie,
                        headerValue = headerValue,
                        isGrid = viewModel.state.isGrid,
                        scope = scope,
                        onLongClick = viewModel::onEvent,
                        onClick = viewModel::onEvent
                    )


                largeSpace(
                    GridItemSpan(
                        if (viewModel.state.isGrid) viewModel.state.maxGridSize
                        else viewModel.state.minGridSize
                    )
                )

                // favourite
                if (viewModel.state.data.all.isFavourite)
                    favourite(
                        span = GridItemSpan(
                            if (viewModel.state.isGrid) viewModel.state.maxGridSize
                            else viewModel.state.minGridSize
                        ),
                        scope = scope,
                        onLongClick = viewModel::onEvent,
                        onClick = viewModel::onEvent
                    )

                largeSpace(
                    GridItemSpan(
                        if (viewModel.state.isGrid) viewModel.state.maxGridSize
                        else viewModel.state.minGridSize
                    )
                )

                // playlist
                if (viewModel.state.data.all.playlist.isNotEmpty())
                    playlist(
                        playlistPrev = viewModel.state.data.all.playlist,
                        sizeIfGrid = if (isSmallPhone) 120.dp else 130.dp,
                        sizeIfList = 80.dp,
                        isCookie = isCookie,
                        headerValue = headerValue,
                        isGrid = viewModel.state.isGrid,
                        scope = scope,
                        onCreatePlaylistClick = viewModel::onEvent,
                        onLongClick = viewModel::onEvent,
                        onClick = viewModel::onEvent
                    )

                largeSpace(
                    GridItemSpan(
                        if (viewModel.state.isGrid) viewModel.state.maxGridSize
                        else viewModel.state.minGridSize
                    )
                )

                // album
                if (viewModel.state.data.all.album.isNotEmpty())
                    album(
                        albums = viewModel.state.data.all.album,
                        sizeIfGrid = if (isSmallPhone) 120.dp else 130.dp,
                        sizeIfList = 80.dp,
                        isCookie = isCookie,
                        headerValue = headerValue,
                        isGrid = viewModel.state.isGrid,
                        scope = scope,
                        onCreateAlbumClick = viewModel::onEvent,
                        onLongClick = viewModel::onEvent,
                        onClick = viewModel::onEvent
                    )


                largeSpace(
                    GridItemSpan(
                        if (viewModel.state.isGrid) viewModel.state.maxGridSize
                        else viewModel.state.minGridSize
                    )
                )

                // artist
                if (viewModel.state.data.all.artist.isNotEmpty()) {
                    artist(
                        artists = viewModel.state.data.all.artist,
                        size = 90.dp,
                        isCookie = isCookie,
                        headerValue = headerValue,
                        isGrid = viewModel.state.isGrid,
                        scope = scope,
                        onAddArtistClick = viewModel::onEvent,
                        onLongClick = viewModel::onEvent,
                        onClick = viewModel::onEvent
                    )
                }
            }
        }
    }

    if (viewModel.state.isBottomSheetOpen)
        LibraryScreenBottomSheet(
            sheetState = sheetState,
            pinnedData = viewModel.state.pinnedData,
            onClick = viewModel::onEvent
        ) {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                viewModel.onEvent(LibraryUiEvent.HideBottomSheet)
            }
        }

    if (viewModel.state.isDialogOpen)
        ItemDeleteDialog(
            text = viewModel.state.pinnedData.name,
            onYesClick = {
                viewModel.onEvent(LibraryUiEvent.DeleteDialogClick.DeleteYes)
            },
            onNoClick = {
                viewModel.onEvent(LibraryUiEvent.DeleteDialogClick.DeleteNo)
            }
        )
}