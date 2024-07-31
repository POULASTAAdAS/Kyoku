package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String = "",
)
