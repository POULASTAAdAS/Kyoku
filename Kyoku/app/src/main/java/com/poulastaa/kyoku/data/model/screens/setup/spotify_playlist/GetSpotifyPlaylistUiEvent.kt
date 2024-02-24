package com.poulastaa.kyoku.data.model.screens.setup.spotify_playlist


sealed class GetSpotifyPlaylistUiEvent {
    data class OnLinkEnter(val like: String) : GetSpotifyPlaylistUiEvent()

    data object OnAddButtonClick : GetSpotifyPlaylistUiEvent()
    data object OnSkipClick : GetSpotifyPlaylistUiEvent()

    data class EmitToast(val message: String) : GetSpotifyPlaylistUiEvent()
    data object SomethingWentWrong : GetSpotifyPlaylistUiEvent()

    data object OnContinueClick : GetSpotifyPlaylistUiEvent()

    data class OnPlaylistClick(val name: String) : GetSpotifyPlaylistUiEvent()
}