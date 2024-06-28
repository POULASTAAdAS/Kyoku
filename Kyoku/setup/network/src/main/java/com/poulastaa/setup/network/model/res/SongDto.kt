package com.poulastaa.setup.network.model.res

import kotlinx.serialization.Serializable

@Serializable
data class SongDto(
    val id: Long,
    val coverImage: String,
    val title: String,
    val artistName: String,
    val releaseYear: String,
    val masterPlaylistUrl: String,
)
