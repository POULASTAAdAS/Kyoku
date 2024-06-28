package com.poulastaa.setup.network.model.req

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistReq(
    val urL: String,
)
