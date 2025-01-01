package com.poulastaa.core.domain.model

data class DtoSongInfo(
    val id: Long = -1,
    val releaseYear: Int = -1,
    val composer: String? = null,
    val popularity: Long = -1,
)
