package com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre

import kotlinx.serialization.Serializable

@Serializable
data class StoreGenreResponse(
    val status: GenreResponseStatus
)
