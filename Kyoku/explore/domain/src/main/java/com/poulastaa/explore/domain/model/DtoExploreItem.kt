package com.poulastaa.explore.domain.model

data class DtoExploreItem(
    val id: Long = -1,
    val title: String = "",
    val poster: String? = "",
    val releaseYear: Int = 0,
    val artist: String? = null,
)