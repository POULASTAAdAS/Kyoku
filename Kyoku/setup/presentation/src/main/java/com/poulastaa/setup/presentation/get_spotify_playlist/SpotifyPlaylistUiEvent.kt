package com.poulastaa.setup.presentation.get_spotify_playlist


sealed interface SpotifyPlaylistUiEvent {
    data class OnLinkChange(val data: String) : SpotifyPlaylistUiEvent
    data object OnLikeSubmit : SpotifyPlaylistUiEvent
    data class OnPlaylistClick(val id: Long) : SpotifyPlaylistUiEvent

    data object OnSkipClick : SpotifyPlaylistUiEvent
}