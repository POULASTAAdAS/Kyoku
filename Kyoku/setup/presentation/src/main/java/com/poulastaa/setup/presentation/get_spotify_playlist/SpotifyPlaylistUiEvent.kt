package com.poulastaa.setup.presentation.get_spotify_playlist

import android.graphics.Bitmap

sealed interface SpotifyPlaylistUiEvent {
    data class OnLinkChange(val data: String) : SpotifyPlaylistUiEvent
    data object OnLikeSubmit : SpotifyPlaylistUiEvent
    data class OnPlaylistClick(val id: Long) : SpotifyPlaylistUiEvent

    data class StoreImageColor(
        val id: Long,
        val bitmap: Bitmap,
    ) : SpotifyPlaylistUiEvent

    data object OnSkipClick : SpotifyPlaylistUiEvent
}