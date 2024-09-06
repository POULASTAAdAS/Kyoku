package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PagingArtistResDto(
    val list: List<ArtistDto> = emptyList(),
)
