package com.poulastaa.core.presentation.ui.components.crate_playlist

internal sealed interface CreatePlaylistUiAction {
    data class OnPlaylistNameChange(val message: String) : CreatePlaylistUiAction
    data object OnSaveClick : CreatePlaylistUiAction
    data object OnCancelClick : CreatePlaylistUiAction
}