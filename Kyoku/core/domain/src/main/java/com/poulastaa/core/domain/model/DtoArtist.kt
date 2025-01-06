package com.poulastaa.core.domain.model

data class DtoArtist(
    val id: Long,
    val name: String,
    val coverImage: String?,
    val popularity: Long,
    val genre: DtoGenre?,
    val country: DtoCountry?,
)