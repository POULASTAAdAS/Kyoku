package com.poulastaa.data.model.setup.genre

import kotlinx.serialization.Serializable

@Serializable
class SuggestGenreResponse(
    val status: GenreResponseStatus,
    val genreList: List<String> = emptyList()
)
