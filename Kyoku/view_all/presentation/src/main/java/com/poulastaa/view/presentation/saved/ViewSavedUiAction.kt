package com.poulastaa.view.presentation.saved

import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.presentation.designsystem.model.ItemClickType

internal sealed interface ViewSavedUiAction {
    data object OnClearSelectedDialogToggle : ViewSavedUiAction
    data object OnClearAllSelectedClick : ViewSavedUiAction
    data object OnDeleteAllToggleClick : ViewSavedUiAction
    data object OnDeleteAllConformClick : ViewSavedUiAction
    data class OnItemClick(val id: Long, val clickType: ItemClickType) : ViewSavedUiAction
    data class OnSelectToggle(val id: Long) : ViewSavedUiAction
    data object OnAddNewItemClick : ViewSavedUiAction
    data class OnNewPlaylistCreated(val playlistId: PlaylistId) : ViewSavedUiAction
}