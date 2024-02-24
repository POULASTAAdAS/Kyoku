package com.poulastaa.data.model.setup.suggest_genre

import kotlinx.serialization.Serializable

@Serializable
enum class SuggestGenreResponseStatus {
    SUCCESS,
    FAILURE
}