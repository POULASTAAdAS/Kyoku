package com.poulastaa.data.model.spotify

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyPlaylistResponse(
    val listOfResponseSong: List<ResponseSong> = emptyList()
)
