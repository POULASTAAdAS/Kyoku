package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class PrevAlbumDto(
    val albumId: Long,
    val name: String,
    val coverImage: String,
)
