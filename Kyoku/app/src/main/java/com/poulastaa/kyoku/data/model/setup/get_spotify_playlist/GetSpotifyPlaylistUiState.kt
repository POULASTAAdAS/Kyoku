package com.poulastaa.kyoku.data.model.setup.get_spotify_playlist

data class GetSpotifyPlaylistUiState(
    val link: String = "",
    val linkSupportingText: String = "",
    val isLinkError: Boolean = false,

    val isFirstPlaylist: Boolean = true,

    val isMakingApiCall: Boolean = false,
    val isInternetAvailable: Boolean = false
)
