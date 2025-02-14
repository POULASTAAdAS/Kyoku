package com.poulastaa.core.domain.model

data class DtoPrevArtist(
    val id: ArtistId,
    val name: String,
    val cover: String? = null,
)
