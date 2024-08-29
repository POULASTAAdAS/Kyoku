package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlaylistReq(
    val songId: Long,
    val playlistIdList: Map<Long, Boolean>,
)