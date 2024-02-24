package com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre

import kotlinx.serialization.Serializable

@Serializable
class SuggestGenreResponse(
    val status: SuggestGenreResponseStatus,
    val genreList: List<String>
)
