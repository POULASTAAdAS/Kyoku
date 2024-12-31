package com.poulastaa.user.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ImportSpotifyPlaylistReq(
    val playlistId: String,
)
