package com.poulastaa.network.mapper

import com.poulastaa.core.domain.model.ArtistSingleData
import com.poulastaa.network.model.ArtistSingleDataDto

fun ArtistSingleDataDto.toArtistSingleData() = ArtistSingleData(
    id = this.id,
    title = this.title,
    coverImage = this.coverImage,
    releaseYear = this.releaseYear
)