package com.poulastaa.user.network.mapper

import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.network.mapper.toResponseGenre
import com.poulastaa.user.network.model.SuggestedGenreRes

fun List<DtoGenre>.toSuggestGenreDto() = SuggestedGenreRes(
    list = this.map { it.toResponseGenre() }
)
