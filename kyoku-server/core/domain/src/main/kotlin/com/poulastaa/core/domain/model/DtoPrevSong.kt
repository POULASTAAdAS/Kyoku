package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.repository.SongId
import com.poulastaa.core.domain.utils.Constants.POSTER_PARAM

data class DtoPrevSong(
    val id: SongId,
    val title: String,
    private val rawPoster: String?,
) {
    private val baseUrl = System.getenv("BASE_URL").dropLast(1)

    val poster = rawPoster?.let { "$baseUrl${EndPoints.Poster.SongPoster.route}?$POSTER_PARAM=$rawPoster" }
}
