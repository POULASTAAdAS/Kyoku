package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.utils.Constants

data class DtoSearchItem(
    val id: Long,
    val title: String,
    private val rawPoster: String?,
    val isTypeSong: Boolean = true,
    val releaseYear: Int? = null,
    val artist: String? = null,
) {
    private val baseUrl = System.getenv("BASE_URL").dropLast(1)

    val poster = rawPoster?.let {
        if (it.startsWith("http")) it
        else "$baseUrl${if (isTypeSong) EndPoints.Poster.SongPoster.route else EndPoints.Poster.ArtistPoster.route}?${Constants.POSTER_PARAM}=$rawPoster"
    }
}