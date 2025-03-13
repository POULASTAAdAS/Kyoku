package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.utils.Constants.POSTER_PARAM

data class DtoPrevArtist(
    val id: ArtistId,
    val name: String,
    private val rawCover: String?,
) {
    private val baseUrl = System.getenv("BASE_URL").dropLast(1)

    val cover: String? = rawCover?.let {
        if (it.startsWith("http")) it
        else "$baseUrl${EndPoints.Poster.ArtistPoster.route}?$POSTER_PARAM=$rawCover"
    }
}