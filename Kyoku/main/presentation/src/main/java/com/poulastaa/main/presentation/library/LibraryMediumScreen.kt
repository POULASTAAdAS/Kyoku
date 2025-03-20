package com.poulastaa.main.presentation.library

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import com.poulastaa.core.presentation.designsystem.BOTTOM_BAR_HEIGHT
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.main.presentation.library.components.LibraryLoadingScreen
import com.poulastaa.main.presentation.library.components.libraryFilterRow
import com.poulastaa.main.presentation.library.components.libraryHeading
import com.poulastaa.main.presentation.library.components.libraryLazyGridItem
import com.poulastaa.main.presentation.main.components.MAIN_TOP_BAR_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LibraryMediumScreen(
    state: LibraryUiState,
    topAppBarScroll: TopAppBarScrollBehavior,
    onAction: (LibraryUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        AnimatedContent(state.canShowUi) { loadingState ->
            when (loadingState) {
                true -> LazyVerticalGrid(
                    columns = GridCells.Fixed(
                        when (state.viewType) {
                            UiLibraryViewType.GRID -> 5
                            UiLibraryViewType.LIST -> 6
                        }
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = ThemModeChanger.getGradiantBackground()
                            )
                        )
                        .padding(paddingValues)
                        .nestedScroll(topAppBarScroll.nestedScrollConnection),
                    contentPadding = PaddingValues(
                        top = MAIN_TOP_BAR_PADDING,
                        start = MaterialTheme.dimens.medium1,
                        end = MaterialTheme.dimens.medium1,
                        bottom = MaterialTheme.dimens.medium1
                    ),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                ) {
                    libraryFilterRow(state, onAction, haptic)

                    if (state.filterType == UiLibraryFilterType.PLAYLIST ||
                        state.filterType == UiLibraryFilterType.ALL
                    ) {
                        libraryHeading(R.string.playlist) {
                            onAction(
                                LibraryUiAction.OnEditSavedItemTypeClick(
                                    UiLibraryEditSavedItemType.PLAYLIST
                                )
                            )
                        }

                        libraryLazyGridItem(
                            span = 2,
                            items = state.playlist,
                            viewType = state.viewType,
                            onAction = onAction
                        )
                    }

                    if (state.filterType == UiLibraryFilterType.ALBUM ||
                        state.filterType == UiLibraryFilterType.ALL
                    ) {
                        libraryHeading(R.string.album) {
                            onAction(
                                LibraryUiAction.OnEditSavedItemTypeClick(
                                    UiLibraryEditSavedItemType.ALBUM
                                )
                            )
                        }

                        libraryLazyGridItem(
                            span = 2,
                            items = state.album,
                            viewType = state.viewType,
                            onAction = onAction
                        )
                    }

                    if (state.filterType == UiLibraryFilterType.ARTIST ||
                        state.filterType == UiLibraryFilterType.ALL
                    ) {
                        libraryHeading(R.string.artist) {
                            onAction(
                                LibraryUiAction.OnEditSavedItemTypeClick(
                                    UiLibraryEditSavedItemType.ARTIST
                                )
                            )
                        }

                        libraryLazyGridItem(
                            span = 2,
                            items = state.artist,
                            viewType = state.viewType,
                            onAction = onAction
                        )
                    }

                    item {
                        Spacer(Modifier.height(BOTTOM_BAR_HEIGHT + MaterialTheme.dimens.medium1))
                    }
                }

                false -> LibraryLoadingScreen(paddingValues, topAppBarScroll, 5)
            }
        }
    }
}