package com.poulastaa.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    val id: Long,
    val name: String,
    val coverImage: String?,
)
