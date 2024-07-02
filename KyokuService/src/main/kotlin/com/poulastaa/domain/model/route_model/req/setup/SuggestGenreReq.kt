package com.poulastaa.domain.model.route_model.req.setup

import kotlinx.serialization.Serializable

@Serializable
data class SuggestGenreReq(
    val listOfSentGenreId: List<Int>,
)
