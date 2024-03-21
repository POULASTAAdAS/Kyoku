package com.poulastaa.data.model.setup.spotify

import com.poulastaa.data.model.common.ResponseSong
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyPlaylistResponse(
    val status: HandleSpotifyPlaylistStatus = HandleSpotifyPlaylistStatus.FAILURE,
    val id: Long = 0,
    val name: String = "",
    val listOfResponseSong: List<ResponseSong> = emptyList()
)