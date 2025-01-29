package com.poulastaa.core.database.entity.model

import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.GenreId

internal data class DtoGenreArtistRelation(
    val genreId: GenreId,
    val artistId: ArtistId,
    val popularity: Long,
    val genre: String,
)