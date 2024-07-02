package com.poulastaa.domain.model.route_model.req.setup

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistReq(
    val playlistId: String,
)
