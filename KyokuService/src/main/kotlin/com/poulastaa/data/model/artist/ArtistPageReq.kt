package com.poulastaa.data.model.artist

import kotlinx.serialization.Serializable

@Serializable
data class ArtistPageReq(
    val name: String = "",
    val page: Int = 1,
    val pageSize: Int = 20
)
