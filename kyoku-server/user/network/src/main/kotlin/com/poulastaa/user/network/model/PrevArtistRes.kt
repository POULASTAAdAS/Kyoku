package com.poulastaa.user.network.model

import com.poulastaa.core.domain.repository.ArtistId
import kotlinx.serialization.Serializable

@Serializable
data class PrevArtistRes(
    val id: ArtistId,
    val name: String,
    val cover: String?,
)
