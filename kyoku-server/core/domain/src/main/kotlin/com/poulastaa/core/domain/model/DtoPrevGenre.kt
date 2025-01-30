package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.repository.GenreId

data class DtoPrevGenre(
    val id: GenreId,
    val name: String,
)
