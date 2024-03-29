package com.poulastaa.kyoku.data.model.screens.setup.spotify_playlist

import com.poulastaa.kyoku.data.model.database.SongInfo

data class GetSpotifyPlaylistUiState(
    val link: String = "",
    val linkSupportingText: String = "",
    val isLinkError: Boolean = false,

    val isFirstPlaylist: Boolean = true,

    val isMakingApiCall: Boolean = false,
    val isInternetAvailable: Boolean = false,
    val listOfPlaylist: List<SpotifyUiPlaylist> = emptyList(),
    val canSkip: Boolean = true
)


data class SpotifyUiPlaylist(
    val name: String = "",
    val songs: List<SongInfo> = emptyList(),
    var isExpanded: Boolean = false
)