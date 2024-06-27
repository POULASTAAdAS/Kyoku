package com.poulastaa.setup.presentation.get_spotify_playlist

import com.poulastaa.core.presentation.ui.model.TextHolder
import com.poulastaa.setup.presentation.get_spotify_playlist.model.UiPlaylist

data class SpotifyPlaylistUiState(
    val isMakingApiCall: Boolean = false,
    val link: TextHolder = TextHolder(),

    val playlists: List<UiPlaylist> = emptyList(),
)
