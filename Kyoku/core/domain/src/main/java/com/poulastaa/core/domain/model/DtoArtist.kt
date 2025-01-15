package com.poulastaa.core.domain.model

typealias ArtistId = Long

data class DtoArtist(
    val id: ArtistId,
    val name: String,
    val coverImage: String?,
    val popularity: Long,
    val genre: DtoGenre?,
    val country: DtoCountry?,
)