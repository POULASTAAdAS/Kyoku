package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ArtistPagerDataDto(
    val list: List<ArtistSingleDataDto> = emptyList(),
)

@Serializable
data class ArtistSingleDataDto(
    val id: Long,
    val title: String,
    val coverImage: String,
    val releaseYear: Int,
)
