package com.poulastaa.play.presentation.add_to_playlist

sealed interface AddToPlaylistUiEvent {
    data object CancelSearch : AddToPlaylistUiEvent
    data object EnableSearch : AddToPlaylistUiEvent

    data class OnSearchQueryChange(val query: String) : AddToPlaylistUiEvent

    data object AddNewPlaylist : AddToPlaylistUiEvent

    data object OnFevToggle : AddToPlaylistUiEvent

    data class OnPlaylistClick(val playlistId: Long) : AddToPlaylistUiEvent

    data object OnSaveClick : AddToPlaylistUiEvent

    sealed interface AddNewPlaylistUiEvent : AddToPlaylistUiEvent {
        data object OnSaveClick : AddNewPlaylistUiEvent
        data object OnCancelClick : AddNewPlaylistUiEvent
        data class OnNameChange(val name: String) : AddNewPlaylistUiEvent
    }
}