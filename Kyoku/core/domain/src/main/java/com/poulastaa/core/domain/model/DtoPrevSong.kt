package com.poulastaa.core.domain.model

data class DtoPrevSong(
    val id: SongId = -1,
    val title: String = "",
    val cover: String? = null,
    val releaseYear: Int = -1,
)
