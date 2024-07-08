package com.poulastaa.setup.network.model.res

import com.poulastaa.core.data.model.ArtistDto
import kotlinx.serialization.Serializable

@Serializable
data class SuggestArtistDto(
    val listOfArtist: List<ArtistDto> = emptyList(),
)
