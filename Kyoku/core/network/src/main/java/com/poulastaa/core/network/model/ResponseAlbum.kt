package com.poulastaa.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseAlbum(
    val id: Long,
    val name: String,
    val poster: String?,
)
