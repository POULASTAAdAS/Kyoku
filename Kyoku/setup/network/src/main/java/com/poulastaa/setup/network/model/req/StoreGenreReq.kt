package com.poulastaa.setup.network.model.req

import com.poulastaa.core.domain.repository.genre.GenreId
import kotlinx.serialization.Serializable

@Serializable
data class StoreGenreReq(
    val idList: List<GenreId>,
)
