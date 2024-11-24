package com.poulastaa.core.domain.model

data class ArtistWithPopularity(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String = "",
    val popularity: Long = 0,
)
