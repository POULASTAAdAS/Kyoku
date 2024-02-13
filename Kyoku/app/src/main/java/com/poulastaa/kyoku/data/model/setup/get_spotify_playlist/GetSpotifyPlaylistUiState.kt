package com.poulastaa.kyoku.data.model.setup.get_spotify_playlist

import com.poulastaa.kyoku.data.model.ui.UiPlaylist

data class GetSpotifyPlaylistUiState(
    val link: String = "",
    val linkSupportingText: String = "",
    val isLinkError: Boolean = false,

    val isFirstPlaylist: Boolean = true,

    val isMakingApiCall: Boolean = false,
    val isInternetAvailable: Boolean = false,
    val listOfPlaylist: List<UiPlaylist> = emptyList(),
)
