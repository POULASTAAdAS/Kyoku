package com.poulastaa.core.domain.model

data class DtoAlbum(
    val id: Long = -1,
    val name: String = "",
    val poster: String? = null,
    val popularity: Long,
)
