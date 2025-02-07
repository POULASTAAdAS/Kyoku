package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.utils.Constants.POSTER_PARAM

data class DtoArtist(
    val id: Long,
    val name: String,
    private val rawCoverImage: String?,
    val popularity: Long,
    val genre: DtoGenre?,
    val country: DtoCountry?,
) {
    private val baseUrl = System.getenv("BASE_URL").dropLast(1)

    val coverImage: String? =
        rawCoverImage?.let { "$baseUrl${EndPoints.Poster.ArtistPoster.route}?$POSTER_PARAM=$rawCoverImage" }
}