package com.poulastaa.data.model.setup.suggest_genre

import kotlinx.serialization.Serializable

@Serializable
class SuggestGenreResponse(
    val status: SuggestGenreResponseStatus,
    val genreList: List<String> = emptyList()
)
