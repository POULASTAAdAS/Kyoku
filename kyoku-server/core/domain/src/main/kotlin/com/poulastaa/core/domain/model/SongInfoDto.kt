package com.poulastaa.core.domain.model

data class SongInfoDto(
    val id: Long,
    val releaseYear: Int,
    val composer: String?,
    val popularity: Long,
)
