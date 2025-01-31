package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.repository.AlbumId

data class DtoPrevAlbum(
    val id: AlbumId,
    val name: String,
)