package com.poulastaa.data.model.auth.response.login

import kotlinx.serialization.Serializable

@Serializable
data class PrevAlbumDto(
    val albumId: Long,
    val name: String,
    val coverImage: String,
)
