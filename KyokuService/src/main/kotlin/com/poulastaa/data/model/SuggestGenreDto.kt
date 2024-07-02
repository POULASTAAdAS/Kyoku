package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SuggestGenreDto(
    val listOgGenre: List<GenreDto> = emptyList(),
)
