package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class PrevSongDetailDto(
    val songId: Long,
    val coverImage: String,
    val title: String,
    val artist: String,
)
