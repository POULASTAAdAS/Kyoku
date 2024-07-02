package com.poulastaa.setup.network.model.req

import kotlinx.serialization.Serializable

@Serializable
data class GetArtistReq(
    val sentArtistId: List<Long>,
)
