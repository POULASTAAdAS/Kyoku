package com.poulastaa.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PrevAlbumDto(
    val albumId: Long,
    val name:String,
    val coverImage: String,
)