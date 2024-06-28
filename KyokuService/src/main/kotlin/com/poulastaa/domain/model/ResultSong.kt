package com.poulastaa.domain.model

data class ResultSong(
    val id: Long = -1,
    val coverImage: String = "",
    val title: String = "",
    val releaseYear: Int = -1,
    val masterPlaylistUrl: String = "",
)
