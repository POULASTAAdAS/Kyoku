package com.poulastaa.data.model.setup.artist

import kotlinx.serialization.Serializable

@Serializable
data class StoreArtistReq(
    val data: List<String>
)
