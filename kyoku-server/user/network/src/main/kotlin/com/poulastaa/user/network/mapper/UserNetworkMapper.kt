package com.poulastaa.user.network.mapper

import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.DtoUpsert
import com.poulastaa.core.domain.repository.GenreId
import com.poulastaa.core.network.mapper.toDtoUpsertOperation
import com.poulastaa.core.network.mapper.toResponseGenre
import com.poulastaa.core.network.model.UpsertReq
import com.poulastaa.user.network.model.SuggestedGenreRes

fun List<DtoGenre>.toSuggestGenreDto() = SuggestedGenreRes(
    list = this.map { it.toResponseGenre() }
)

fun UpsertReq<GenreId>.toDtoUpsertGenre() = DtoUpsert<GenreId>(
    id = this.id,
    operation = this.operation.toDtoUpsertOperation()
)