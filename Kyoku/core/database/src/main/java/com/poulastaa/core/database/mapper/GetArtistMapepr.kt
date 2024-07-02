package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.entity.ArtistEntity
import com.poulastaa.core.domain.model.Artist

fun Artist.toArtistEntity() = ArtistEntity(
    id = id,
    name = name,
    coverImage = coverImage ?: ""
)