package com.poulastaa.explore.network.model

data class ResponseExploreItem(
    val id: Long = -1,
    val title: String = "",
    val poster: String = "",
    val releaseYear: Int = 0,
    val artist: String? = null,
)