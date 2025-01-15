package com.poulastaa.core.domain.model

typealias AlbumId = Long

data class DtoAlbum(
    val id: AlbumId = -1,
    val name: String = "",
    val poster: String? = null,
    val popularity: Long,
)
