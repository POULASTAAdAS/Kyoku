package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ViewArtistSongDto(
    val id: Long,
    val title: String,
    val coverImage: String,
    val popularity: Long,
)
