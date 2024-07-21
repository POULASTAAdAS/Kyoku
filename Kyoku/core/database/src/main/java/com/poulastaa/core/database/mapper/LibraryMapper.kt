package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.entity.ArtistEntity
import com.poulastaa.core.domain.model.Artist

fun ArtistEntity.toArtist() = Artist(
    id = id,
    name = name,
    coverImage = coverImage
)