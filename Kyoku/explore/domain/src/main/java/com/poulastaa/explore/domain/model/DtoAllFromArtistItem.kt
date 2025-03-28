package com.poulastaa.explore.domain.model

data class DtoAllFromArtistItem(
    val id: Long = -1,
    val title: String = "",
    val poster: String? = "",
    val releaseYear: Int = 0,
)