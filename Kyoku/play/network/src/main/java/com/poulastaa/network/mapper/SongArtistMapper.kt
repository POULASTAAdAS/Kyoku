package com.poulastaa.network.mapper

import com.poulastaa.core.domain.model.ArtistWithPopularity
import com.poulastaa.network.model.ArtistWithPopularityDto

fun ArtistWithPopularityDto.toArtistWithPopularity() = ArtistWithPopularity(
    id = this.id,
    name = this.name,
    popularity = this.popularity,
    coverImage = this.coverImage
)