package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ArtistWithPopularityDto(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String = "",
    val popularity: Long = 0,
)