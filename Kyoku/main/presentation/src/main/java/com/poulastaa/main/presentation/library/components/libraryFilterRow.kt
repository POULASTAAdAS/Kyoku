package com.poulastaa.main.presentation.library.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ui.SortTypeGridIcon
import com.poulastaa.core.presentation.designsystem.ui.SortTypeListIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.main.presentation.library.LibraryUiAction
import com.poulastaa.main.presentation.library.LibraryUiState
import com.poulastaa.main.presentation.library.UiLibraryFilterType
import com.poulastaa.main.presentation.library.UiLibraryViewType


@OptIn(ExperimentalLayoutApi::class)
internal fun LazyGridScope.libraryFilterRow(
    state: LibraryUiState,
    onAction: (LibraryUiAction) -> Unit,
    haptic: HapticFeedback,
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
}