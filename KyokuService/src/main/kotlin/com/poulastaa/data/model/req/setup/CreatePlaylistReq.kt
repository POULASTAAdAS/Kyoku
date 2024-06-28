package com.poulastaa.data.model.req.setup

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistReq(
    val playlistId: String,
)
