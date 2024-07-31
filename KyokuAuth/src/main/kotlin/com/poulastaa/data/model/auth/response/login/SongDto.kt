package com.poulastaa.data.model.auth.response.login

import kotlinx.serialization.Serializable

@Serializable
data class SongDto(
    val id: Long = -1,
    val coverImage: String = "",
    val title: String = "",
    val artistName: String,
    val releaseYear: Int = -1,
    val masterPlaylistUrl: String = "",
)