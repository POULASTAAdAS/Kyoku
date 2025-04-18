package com.poulastaa.view.presentation.saved

import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.presentation.designsystem.model.ItemClickType

internal sealed interface ViewSavedUiAction {
    data object OnEditToggle : ViewSavedUiAction
    data object OnDeleteAllClick : ViewSavedUiAction
    data class OnItemClick(val id: Long, val clickType: ItemClickType) : ViewSavedUiAction
    data object OnAddNewItemClick : ViewSavedUiAction
    data class OnNewPlaylistCreated(val playlistId: PlaylistId) : ViewSavedUiAction
}