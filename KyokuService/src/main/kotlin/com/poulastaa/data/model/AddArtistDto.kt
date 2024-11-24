package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AddArtistDto(
    val list: List<ArtistDto> = emptyList(),
)
