package com.poulastaa.kyoku.playlist.domain.model

import com.poulastaa.kyoku.playlist.utils.GenreId

data class DtoGenre(
    val id: GenreId = 0,
    val name: String = "",
    val rawCoverImage: String? = null,
    val popularity: Long = 0,
) {
    val coverImage = rawCoverImage
}
