package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SuggestArtistDao(
    val listOfArtist: List<ArtistDto> = emptyList(),
)
