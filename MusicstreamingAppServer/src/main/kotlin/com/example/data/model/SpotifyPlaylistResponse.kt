package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyPlaylistResponse(
    val listOfResponseSong: List<ResponseSong> = emptyList()
)
