package com.poulastaa.main.presentation.library

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemChanger
import com.poulastaa.core.presentation.designsystem.ui.AddIcon
import com.poulastaa.core.presentation.designsystem.ui.SongIcon
import com.poulastaa.core.presentation.designsystem.ui.SortTypeGridIcon
import com.poulastaa.core.presentation.designsystem.ui.SortTypeListIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.main.presentation.components.MainBoxImageCard
import com.poulastaa.main.presentation.library.components.LibraryLoadingScreen
import com.poulastaa.main.presentation.main.components.MAIN_TOP_BAR_PADDING

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun LibraryCompactScreen(
    scroll: TopAppBarScrollBehavior,
    state: LibraryUiState,
    onAction: (LibraryUiAction) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        AnimatedContent(state.canShowUi) { loadingState ->
            when (loadingState) {
                true -> LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = ThemChanger.getGradiantBackground()
                            )
                        )
                        .padding(paddingValues)
                        .nestedScroll(scroll.nestedScrollConnection),
                    contentPadding = PaddingValues(
                        top = MAIN_TOP_BAR_PADDING + MaterialTheme.dimens.medium1,
                        start = MaterialTheme.dimens.medium1,
                        end = MaterialTheme.dimens.medium1,
                        bottom = MaterialTheme.dimens.medium1
                    ),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                ) {
                    item(
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(.85f),
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
                            ) {
                                LibraryFilterChip(
                                    type = UiLibraryFilterType.ALL,
                                    selected = state.filterType == UiLibraryFilterType.ALL,
                                    onClick = {
                                        onAction(
                                            LibraryUiAction.OnFilterTypeToggle(
                                                UiLibraryFilterType.ALL
                                            )
                                        )
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                )

                                LibraryFilterChip(
                                    type = UiLibraryFilterType.ALBUM,
                                    selected = state.filterType == UiLibraryFilterType.ALBUM,
                                    onClick = {
                                        onAction(
                                            LibraryUiAction.OnFilterTypeToggle(
                                                UiLibraryFilterType.ALBUM
                                            )
                                        )
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                )

                                LibraryFilterChip(
                                    type = UiLibraryFilterType.ARTIST,
                                    selected = state.filterType == UiLibraryFilterType.ARTIST,
                                    onClick = {
                                        onAction(
                                            LibraryUiAction.OnFilterTypeToggle(
                                                UiLibraryFilterType.ARTIST
                                            )
                                        )
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                )

                                LibraryFilterChip(
                                    type = UiLibraryFilterType.PLAYLIST,
                                    selected = state.filterType == UiLibraryFilterType.PLAYLIST,
                                    onClick = {
                                        onAction(
                                            LibraryUiAction.OnFilterTypeToggle(
                                                UiLibraryFilterType.PLAYLIST
                                            )
                                        )
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                )
                            }

                            Spacer(Modifier.weight(1f))

                            Card(
                                shape = CircleShape,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 18.dp,
                                    pressedElevation = 0.dp
                                ),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onAction(LibraryUiAction.OnViewTypeToggle)
                                }
                            ) {
                                AnimatedContent(state.viewType) {
                                    when (it) {
                                        UiLibraryViewType.GRID -> Icon(
                                            modifier = Modifier
                                                .padding(8.dp),
                                            imageVector = SortTypeListIcon,
                                            contentDescription = state.viewType.name,
                                            tint = MaterialTheme.colorScheme.primaryContainer
                                        )

                                        UiLibraryViewType.LIST -> Icon(
                                            modifier = Modifier
                                                .padding(8.dp),
                                            imageVector = SortTypeGridIcon,
                                            contentDescription = state.viewType.name,
                                            tint = MaterialTheme.colorScheme.primaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }

                    libraryHeading(R.string.playlist) {
                        onAction(LibraryUiAction.OnEditSavedItemTypeClick(UiLibraryEditSavedItemType.PLAYLIST))
                    }

                    items(state.playlist) { playlist ->
                        MainBoxImageCard(
                            title = playlist.name,
                            urls = playlist.posters,
                            icon = SongIcon,
                            description = playlist.name
                        )
                    }

                    libraryHeading(R.string.album) {
                        onAction(LibraryUiAction.OnEditSavedItemTypeClick(UiLibraryEditSavedItemType.ALBUM))
                    }

                    items(state.album) { album ->
                        MainBoxImageCard(
                            title = album.name,
                            urls = album.posters,
                            icon = SongIcon,
                            description = album.name
                        )
                    }

                    libraryHeading(R.string.artist) {
                        onAction(LibraryUiAction.OnEditSavedItemTypeClick(UiLibraryEditSavedItemType.ARTIST))
                    }

                    items(state.artist) { artist ->
                        MainBoxImageCard(
                            title = artist.name,
                            urls = artist.posters,
                            icon = SongIcon,
                            description = artist.name,
                            shape = CircleShape
                        )
                    }
                }

                false -> LibraryLoadingScreen(scroll, paddingValues, 3)
            }
        }
    }
}

private fun LazyGridScope.libraryHeading(
    @StringRes title: Int,
    onClick: () -> Unit,
) {
    item(
        span = { GridItemSpan(maxLineSpan) }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = MaterialTheme.dimens.medium1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(title),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = onClick,
                ) {
                    Icon(
                        imageVector = AddIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryFilterChip(
    type: UiLibraryFilterType,
    selected: Boolean,
    onClick: () -> Unit,
) {
    ElevatedFilterChip(
        modifier = Modifier.animateContentSize(tween(400)),
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = stringResource(type.id)
            )
        },
        colors = FilterChipDefaults.elevatedFilterChipColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.background,
            labelColor = MaterialTheme.colorScheme.primary
        ),
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = ImageVector.vectorResource(type.icon),
                    contentDescription = type.name,
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
            }
        } else null,
        elevation = FilterChipDefaults.elevatedFilterChipElevation(
            elevation = if (selected) 8.dp else 0.dp,
            pressedElevation = 0.dp
        )
    )
}