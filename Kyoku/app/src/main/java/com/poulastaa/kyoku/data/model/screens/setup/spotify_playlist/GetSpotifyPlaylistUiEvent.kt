package com.poulastaa.kyoku.data.model.screens.setup.spotify_playlist

import android.content.Context


sealed class GetSpotifyPlaylistUiEvent {
    data class OnLinkEnter(val like: String) : GetSpotifyPlaylistUiEvent()

    data class OnAddButtonClick(val context: Context) : GetSpotifyPlaylistUiEvent()
    data object OnSkipClick : GetSpotifyPlaylistUiEvent()

    data class EmitToast(val message: String) : GetSpotifyPlaylistUiEvent()
    data object SomethingWentWrong : GetSpotifyPlaylistUiEvent()

    data object OnContinueClick : GetSpotifyPlaylistUiEvent()

    data class OnPlaylistClick(val name: String) : GetSpotifyPlaylistUiEvent()
}