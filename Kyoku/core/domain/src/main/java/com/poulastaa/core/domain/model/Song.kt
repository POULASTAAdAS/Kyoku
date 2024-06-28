package com.poulastaa.core.domain.model

data class Song(
    val id: Long,
    val coverImage: String,
    val title: String,
    val artistName: String,
    val releaseYear: String,
    val masterPlaylistUrl: String,
    val primary: String = "", // todo add default color
    val background: String = "",
    val onBackground: String = "",
)
