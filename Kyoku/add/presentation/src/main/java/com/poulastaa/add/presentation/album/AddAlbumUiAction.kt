package com.poulastaa.add.presentation.album

internal sealed interface AddAlbumUiAction {
    enum class ClickType {
        ADD,
        VIEW
    }

    data class OnAlbumClick(
        val album: UiAlbum,
        val clickType: ClickType,
    ) : AddAlbumUiAction

    data class OnSearchQueryChange(val query: String) : AddAlbumUiAction

    data class OnSearchFilterTypeChange(
        val filterType: AddAlbumSearchUiFilterType,
    ) : AddAlbumUiAction

    data object OnSaveClick : AddAlbumUiAction
    data object OnClearAllDialogToggle : AddAlbumUiAction
    data object OnSaveCancelClick : AddAlbumUiAction
}