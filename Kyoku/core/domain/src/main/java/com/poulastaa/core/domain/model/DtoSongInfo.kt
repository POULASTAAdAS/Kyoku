package com.poulastaa.core.domain.model

data class DtoSongInfo(
    val songId: SongId = -1,
    val releaseYear: Int = -1,
    val composer: String? = null,
    val popularity: Long = -1,
)