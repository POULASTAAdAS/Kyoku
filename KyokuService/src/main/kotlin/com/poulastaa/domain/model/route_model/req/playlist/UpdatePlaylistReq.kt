package com.poulastaa.domain.model.route_model.req.playlist

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlaylistReq(
    val songId: Long,
    val playlistIdList: Map<Long, Boolean>,
)

