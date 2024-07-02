package com.poulastaa.setup.network.mappers

import com.poulastaa.core.domain.model.Genre
import com.poulastaa.setup.network.model.res.GenreDto

fun GenreDto.toGenre() = Genre(
    id = id,
    name = name
)