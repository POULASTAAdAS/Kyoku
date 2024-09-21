package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AddAlbumReq(
    val list: List<Long>,
)
