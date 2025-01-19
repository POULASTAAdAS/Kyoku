package com.poulastaa.core.domain.model

data class DtoGenre(
    val id: Int = -1,
    val name: String = "",
    val cover: String? = null,
    val popularity: Long = -1,
)
