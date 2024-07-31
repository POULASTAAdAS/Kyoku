package com.poulastaa.data.model.auth.response.login

import kotlinx.serialization.Serializable

@Serializable
data class PrevSongDto(
    val songId: Long,
    val coverImage: String,
)
