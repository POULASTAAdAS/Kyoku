package com.poulastaa.network.model

import com.poulastaa.core.data.model.ArtistDto
import kotlinx.serialization.Serializable

@Serializable
data class AddArtistDto(
    val list: List<ArtistDto>,
)
