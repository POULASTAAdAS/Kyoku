package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class PrevSongDto(
    val songId: Long,
    val coverImage: String,
)
