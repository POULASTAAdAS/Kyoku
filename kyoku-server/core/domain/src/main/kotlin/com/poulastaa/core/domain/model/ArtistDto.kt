package com.poulastaa.core.domain.model

data class ArtistDto(
    val id: Long,
    val name: String,
    val coverImage: String,
    val popularity: Long,
    val genre: GenreDto,
    val country: CountryDto,
)