package com.poulastaa.setup.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ImportSpotifyPlaylistReq(
    val playlistId: String,
)
