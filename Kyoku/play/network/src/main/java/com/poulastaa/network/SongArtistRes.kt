package com.poulastaa.network

import com.poulastaa.network.model.ArtistWithPopularityDto
import kotlinx.serialization.Serializable

@Serializable
data class SongArtistRes(
    val list: List<ArtistWithPopularityDto> = emptyList(),
)
