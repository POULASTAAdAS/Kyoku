package com.poulastaa.data.model.common

import kotlinx.serialization.Serializable

@Serializable
data class ResponseSong(
    val id: Long = -1,
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val coverImage: String = "",
    val masterPlaylistUrl: String = "",
    val totalTime: String = "",
    val date: String = ""
)
