package com.poulastaa.setup.network.mappers

import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.data.model.ArtistDto

fun ArtistDto.toArtist() = Artist(
    id = id,
    name = name,
    coverImage = coverImage
)