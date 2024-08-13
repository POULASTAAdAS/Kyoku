package com.poulastaa.domain.model.route_model.req.favourite

import kotlinx.serialization.Serializable

@Serializable
data class UpdateFavouriteReq(
    val songId: Long,
    val opp: Boolean,
)
