package com.poulastaa.kyoku.data.model.api.service

import com.poulastaa.kyoku.data.model.api.service.ResponseSong
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyPlaylistResponse(
    val status: HandleSpotifyPlaylistStatus,
    val listOfResponseSong: List<ResponseSong>
)
