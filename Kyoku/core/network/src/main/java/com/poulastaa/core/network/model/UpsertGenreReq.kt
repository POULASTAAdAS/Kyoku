package com.poulastaa.core.network.model

import com.poulastaa.core.domain.model.GenreId
import kotlinx.serialization.Serializable

@Serializable
data class UpsertGenreReq(
    val data: UpsertReq<GenreId>,
)
