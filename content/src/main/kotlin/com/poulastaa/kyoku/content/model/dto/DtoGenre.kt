package com.poulastaa.kyoku.content.model.dto

import com.poulastaa.kyoku.content.utils.GenreId

data class DtoGenre(
    val id: GenreId = 0,
    val type: String = "",
    private val rawPoster: String? = null,
    val popularity: Long = 0,
) {
    val poster = rawPoster // TODO change
}
