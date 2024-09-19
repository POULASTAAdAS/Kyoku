package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SongArtistRes(
    val list: List<ArtistWithPopularityDto> = emptyList(),
)
