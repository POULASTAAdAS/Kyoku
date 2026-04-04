package com.poulastaa.kyoku.search.domain.model.dto

import com.poulastaa.kyoku.search.utils.ArtistId

data class DtoArtist(
    val id: ArtistId = 0,
    val name: String = "",
    val cover: String? = null,
    val popularity: Long = 0,
)
