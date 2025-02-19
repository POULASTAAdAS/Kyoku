package com.poulastaa.core.network.model

import com.poulastaa.core.domain.model.ArtistId
import kotlinx.serialization.Serializable

@Serializable
data class UpsertArtistReq(
    val data: UpsertReq<ArtistId>,
)
