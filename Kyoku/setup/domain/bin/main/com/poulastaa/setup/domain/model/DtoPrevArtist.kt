package com.poulastaa.setup.domain.model

import com.poulastaa.core.domain.model.ArtistId

data class DtoPrevArtist(
    val id: ArtistId,
    val name: String,
    val cover: String? = null,
)
