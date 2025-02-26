package com.poulastaa.main.presentation.library

internal sealed interface LibraryUiAction {
    data class OnFilterTypeToggle(val type: UiLibraryFilterType) : LibraryUiAction
    data object OnViewTypeToggle : LibraryUiAction
    data class OnEditSavedItemTypeClick(val type: UiLibraryEditSavedItemType) : LibraryUiAction
}