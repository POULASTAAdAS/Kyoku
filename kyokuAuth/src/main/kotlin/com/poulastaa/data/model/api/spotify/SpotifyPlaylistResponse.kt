package com.poulastaa.data.model.api.spotify

import com.example.data.model.ResponseSong
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyPlaylistResponse(
    val listOfResponseSong: List<ResponseSong> = emptyList()
)
