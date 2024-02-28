package com.poulastaa.data.model.setup.artist

import kotlinx.serialization.Serializable

@Serializable
data class StoreArtistResponse(
    val status: ArtistResponseStatus = ArtistResponseStatus.FAILURE
)
