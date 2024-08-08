package com.poulastaa.domain.model.route_model.req.playlist

import kotlinx.serialization.Serializable

@Serializable
data class SavePlaylistReq(
    val idList: List<Long>,
    val name: String,
    val type: String,
)