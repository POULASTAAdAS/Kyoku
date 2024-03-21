package com.poulastaa.kyoku.data.model.api.service.setup.spotiry_playlist

import com.poulastaa.kyoku.data.model.api.service.ResponseSong
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyPlaylistResponse(
    val status: HandleSpotifyPlaylistStatus = HandleSpotifyPlaylistStatus.FAILURE,
    val id: Long = 0,
    val name: String = "",
    val listOfResponseSong: List<ResponseSong> = emptyList()
)
