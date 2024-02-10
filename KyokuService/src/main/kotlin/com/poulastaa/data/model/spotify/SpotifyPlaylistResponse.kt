package com.poulastaa.data.model.spotify

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyPlaylistResponse(
    val status: HandleSpotifyPlaylistStatus = HandleSpotifyPlaylistStatus.FAILURE,
    val listOfResponseSong: List<ResponseSong> = emptyList()
)
