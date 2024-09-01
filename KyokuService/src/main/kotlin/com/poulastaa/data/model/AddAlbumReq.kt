package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AddAlbumReq(
    val list: List<Long>,
)
