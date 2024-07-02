package com.poulastaa.setup.network.model.req

import com.poulastaa.core.domain.genre.GenreId
import kotlinx.serialization.Serializable

@Serializable
data class GetGenreReq(
    val listOfSentGenreId: List<GenreId>,
)
