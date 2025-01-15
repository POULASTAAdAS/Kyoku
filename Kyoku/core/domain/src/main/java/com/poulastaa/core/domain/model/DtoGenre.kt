package com.poulastaa.core.domain.model

typealias GenreId = Int

data class DtoGenre(
    val id: GenreId = -1,
    val name: String = "",
    val popularity: Long = -1,
)
