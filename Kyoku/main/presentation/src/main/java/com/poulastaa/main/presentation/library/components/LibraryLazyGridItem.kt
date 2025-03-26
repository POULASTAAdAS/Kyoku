package com.poulastaa.main.presentation.library.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.noRippleCombineClickable
import com.poulastaa.core.presentation.designsystem.ui.SongIcon
import com.poulastaa.main.presentation.components.MainBoxImageCard
import com.poulastaa.main.presentation.components.UiSaveItemType
import com.poulastaa.main.presentation.components.UiSavedItem
import com.poulastaa.main.presentation.library.LibraryUiAction
import com.poulastaa.main.presentation.library.UiLibraryViewType

internal fun LazyGridScope.libraryLazyGridItem(
    span: Int,
    items: List<UiSavedItem>,
    viewType: UiLibraryViewType,
    onAction: (LibraryUiAction.OnItemClick) -> Unit,
) {
    items(
        items,
        span = if (viewType == UiLibraryViewType.LIST) {
            {
                GridItemSpan(span)
            }
        } else null
    ) { item ->
        AnimatedContent(viewType) {
            when (it) {
                UiLibraryViewType.GRID -> MainBoxImageCard(
                    modifier = Modifier.noRippleCombineClickable(
                        onClick = {
                            onAction(
                                LibraryUiAction.OnItemClick(
                                    item.id,
                                    item.type,
                                    ItemClickType.CLICK
                                )
                            )
                        },
                        onLongClick = {
                            onAction(
                                LibraryUiAction.OnItemClick(
                                    item.id,
                                    item.type,
                                    ItemClickType.LONG_CLICK
                                )
                            )
                        }
                    ),
                    title = item.name,
                    urls = item.posters,
                    icon = SongIcon,
                    description = item.name,
                    shape = if (item.type == UiSaveItemType.ARTIST) CircleShape
                    else MaterialTheme.shapes.small
                )

                UiLibraryViewType.LIST -> LibraryListItem(
                    modifier = Modifier
                        .height(80.dp)
                        .noRippleCombineClickable(
                            onClick = {
                                onAction(
                                    LibraryUiAction.OnItemClick(
                                        item.id,
                                        item.type,
                                        ItemClickType.CLICK
                                    )
                                )
                            },
                            onLongClick = {
                                onAction(
                                    LibraryUiAction.OnItemClick(
                                        item.id,
                                        item.type,
                                        ItemClickType.LONG_CLICK
                                    )
                                )
                            }
                        ),
                    title = item.name,
                    posters = item.posters,
                    type = item.type,
                    shape = if (item.type == UiSaveItemType.ARTIST) CircleShape
                    else MaterialTheme.shapes.small
                )
            }
        }
    }
}