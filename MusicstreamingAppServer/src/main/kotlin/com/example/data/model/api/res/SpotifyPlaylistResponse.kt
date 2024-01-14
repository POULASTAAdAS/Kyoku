package com.example.data.model.api.res

import com.example.data.model.ResponseSong
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyPlaylistResponse(
    val listOfResponseSong: List<ResponseSong> = emptyList()
)
