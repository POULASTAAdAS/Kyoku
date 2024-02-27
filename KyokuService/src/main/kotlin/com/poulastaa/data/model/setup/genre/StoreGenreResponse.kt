package com.poulastaa.data.model.setup.genre

import kotlinx.serialization.Serializable

@Serializable
data class StoreGenreResponse(
    val status: GenreResponseStatus
)
