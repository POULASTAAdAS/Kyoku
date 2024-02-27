package com.poulastaa.data.model.setup.genre

import kotlinx.serialization.Serializable

@Serializable
enum class GenreResponseStatus {
    SUCCESS,
    FAILURE
}