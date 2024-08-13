package com.poulastaa.play.presentation.add_to_playlist

sealed interface AddToPlaylistUiEvent {
    data object CancelSearch : AddToPlaylistUiEvent
    data object EnableSearch : AddToPlaylistUiEvent

    data object OnFevToggle : AddToPlaylistUiEvent

    data class OnPlaylistClick(val playlistId: Long) : AddToPlaylistUiEvent

    data object OnSaveClick : AddToPlaylistUiEvent
}