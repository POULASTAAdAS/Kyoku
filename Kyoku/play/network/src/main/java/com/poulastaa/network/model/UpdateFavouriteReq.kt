package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateFavouriteReq(
    val songId: Long,
    val opp: Boolean,
)