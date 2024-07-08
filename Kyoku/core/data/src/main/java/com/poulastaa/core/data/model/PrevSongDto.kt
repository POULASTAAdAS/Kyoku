package com.poulastaa.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PrevSongDto(
    val songId: Long,
    val coverImage: String,
)
