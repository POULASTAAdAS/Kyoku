package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PagingAlbumDto(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String = "",
    val artist: String = "",
    val releaseYear: String = "",
)
