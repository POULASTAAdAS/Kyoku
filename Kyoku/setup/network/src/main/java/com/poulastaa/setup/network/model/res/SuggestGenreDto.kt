package com.poulastaa.setup.network.model.res

import kotlinx.serialization.Serializable

@Serializable
data class SuggestGenreDto(
    val listOgGenre: List<GenreDto> = emptyList(),
)