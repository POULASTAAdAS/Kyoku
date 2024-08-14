package com.poulastaa.domain.model.route_model.req.playlist

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistWithSongReq(
    val name: String,
    val songId: Long,
)
