package com.poulastaa.main.presentation.library

import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.main.presentation.components.UiSaveItemType

internal sealed interface LibraryUiAction {
    data class OnFilterTypeToggle(val type: UiLibraryFilterType) : LibraryUiAction
    data object OnViewTypeToggle : LibraryUiAction
    data class OnEditSavedItemTypeClick(val type: UiLibraryEditSavedItemType) : LibraryUiAction
    data class OnItemClick(
        val id: Long, val type: UiSaveItemType,
        val clickType: ItemClickType,
    ) : LibraryUiAction
}