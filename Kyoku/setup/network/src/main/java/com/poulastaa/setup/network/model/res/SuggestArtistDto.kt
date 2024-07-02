package com.poulastaa.setup.network.model.res

import kotlinx.serialization.Serializable

@Serializable
data class SuggestArtistDto(
    val listOfArtist: List<ArtistDto> = emptyList(),
)
