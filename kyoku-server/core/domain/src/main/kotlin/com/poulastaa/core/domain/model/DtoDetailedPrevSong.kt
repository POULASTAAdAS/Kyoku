package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.repository.SongId

data class DtoDetailedPrevSong(
    val id: SongId = -1,
    val title: String = "",
    val poster: String? = null,
    val artists: String? = null,
    val releaseYear: Int = -1,
)