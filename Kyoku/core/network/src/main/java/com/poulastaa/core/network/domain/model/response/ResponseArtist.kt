package com.poulastaa.core.network.domain.model.response

import com.poulastaa.core.domain.utils.ArtistId
import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtist(
    val artistId: ArtistId,
    val name: String,
    val poster: String?,
    val popularity: Long,
)