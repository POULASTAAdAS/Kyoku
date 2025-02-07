package com.poulastaa.user.network.model

import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.network.model.UpsertReq
import kotlinx.serialization.Serializable

@Serializable
data class UpsertArtistReq(
    val data: UpsertReq<ArtistId>,
)