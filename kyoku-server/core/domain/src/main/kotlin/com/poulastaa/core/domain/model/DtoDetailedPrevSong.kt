package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.repository.SongId
import com.poulastaa.core.domain.utils.Constants.POSTER_PARAM

data class DtoDetailedPrevSong(
    val id: SongId = -1,
    val title: String = "",
    private val rawPoster: String? = null,
    val artists: String? = null,
    val releaseYear: Int = -1,
) {
    private val baseUrl = System.getenv("BASE_URL").dropLast(1)

    val poster = rawPoster?.let {
        if (it.startsWith("http")) it
        else "$baseUrl${EndPoints.Poster.SongPoster.route}?$POSTER_PARAM=$rawPoster"
    }
}