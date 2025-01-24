package com.poulastaa.setup.network.model

import com.poulastaa.core.domain.model.ArtistId
import kotlinx.serialization.Serializable

@Serializable
data class PrevArtist(
    val id: ArtistId,
    val name: String,
    val cover: String?,
)
