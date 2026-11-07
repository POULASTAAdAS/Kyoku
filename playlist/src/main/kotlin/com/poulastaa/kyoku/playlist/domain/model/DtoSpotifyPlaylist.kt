package com.poulastaa.kyoku.playlist.domain.model

import com.poulastaa.kyoku.playlist.utils.SpotifySongTitle

data class DtoSpotifyTrack(
    val position: Int,
    val title: SpotifySongTitle,
    val artists: List<String>,
    val album: String?,
)

data class DtoSpotifyPlaylist(
    val name: String?,
    val tracks: List<DtoSpotifyTrack>,
)
