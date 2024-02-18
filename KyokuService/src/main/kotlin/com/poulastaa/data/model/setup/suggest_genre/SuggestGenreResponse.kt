package com.poulastaa.data.model.setup.suggest_genre

import kotlinx.serialization.Serializable

@Serializable
class SuggestGenreResponse(
    val genre: String,
    val artistUrl: String
)
