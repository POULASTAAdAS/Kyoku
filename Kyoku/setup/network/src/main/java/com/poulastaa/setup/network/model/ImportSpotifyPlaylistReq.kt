package com.poulastaa.setup.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ImportSpotifyPlaylistReq(
    val playlistId: String,
)
