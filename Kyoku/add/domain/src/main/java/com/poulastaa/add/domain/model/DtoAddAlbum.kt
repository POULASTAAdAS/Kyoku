package com.poulastaa.add.domain.model

import com.poulastaa.core.domain.model.AlbumId

data class DtoAddAlbum(
    val id: AlbumId = -1,
    val title: String = "",
    val poster: String? = "",
    val releaseYear: Int = 0,
    val artist: String? = null,
    val isSelected: Boolean = false,
)
