package com.poulastaa.add.domain.model

data class DtoAddArtist(
    val id: Long = -1,
    val title: String = "",
    val poster: String? = "",
    val releaseYear: Int = 0,
    val isSelected: Boolean = false,
)
