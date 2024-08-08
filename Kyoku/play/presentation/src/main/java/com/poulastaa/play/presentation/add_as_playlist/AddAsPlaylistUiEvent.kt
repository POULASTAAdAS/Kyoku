package com.poulastaa.play.presentation.add_as_playlist

sealed interface AddAsPlaylistUiEvent {
    data class OnNameChange(val name: String) : AddAsPlaylistUiEvent
    data object OnSubmit : AddAsPlaylistUiEvent
    data object OnCancel : AddAsPlaylistUiEvent
}