package com.poulastaa.kyoku.data.model.setup.get_spotify_playlist


sealed class GetSpotifyPlaylistUiEvent {
    data class OnLinkEnter(val like: String) : GetSpotifyPlaylistUiEvent()

    data object OnAddButtonClick : GetSpotifyPlaylistUiEvent()
    data object OnSkipClick : GetSpotifyPlaylistUiEvent()

    data class EmitToast(val message: String) : GetSpotifyPlaylistUiEvent()
    data object SomethingWentWrong: GetSpotifyPlaylistUiEvent()
}