package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SavePlaylistReq(
    val idList: List<Long>,
    val name: String,
    val type: String,
)
