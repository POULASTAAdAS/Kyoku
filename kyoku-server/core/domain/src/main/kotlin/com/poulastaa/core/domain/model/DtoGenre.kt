package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.utils.Constants.POSTER_PARAM

data class DtoGenre(
    val id: Int = -1,
    val name: String = "",
    private val rawCover: String? = null,
    val popularity: Long = -1,
) {
    private val baseUrl = System.getenv("BASE_URL").dropLast(1)

    val cover = rawCover?.let { "$baseUrl${EndPoints.Poster.GenrePoster.route}?$POSTER_PARAM=$rawCover" }
}
