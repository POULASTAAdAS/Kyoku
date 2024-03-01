package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponseSong(
    val title: String = "",
    val coverImage: String = "",
    val artist: String = "",
    val album: String = "",
)
