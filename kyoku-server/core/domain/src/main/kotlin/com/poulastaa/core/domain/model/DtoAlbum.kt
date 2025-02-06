package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.utils.Constants.POSTER_PARAM

data class DtoAlbum(
    val id: Long = -1,
    val name: String = "",
    private val rawPoster: String? = null,
    val popularity: Long,
) {
    private val baseUrl = System.getenv("BASE_URL").dropLast(1)
    val poster = rawPoster?.let { "$baseUrl${EndPoints.Poster.SongPoster.route}?$POSTER_PARAM=$rawPoster" }
}
