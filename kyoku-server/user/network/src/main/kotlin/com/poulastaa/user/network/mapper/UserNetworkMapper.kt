package com.poulastaa.user.network.mapper

import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoUpsert
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.GenreId
import com.poulastaa.core.network.mapper.toDtoUpsertOperation
import com.poulastaa.core.network.mapper.toResponseGenre
import com.poulastaa.core.network.model.UpsertReq
import com.poulastaa.user.network.model.PrevArtistRes
import com.poulastaa.user.network.model.SuggestedArtistRes
import com.poulastaa.user.network.model.SuggestedGenreRes

fun List<DtoGenre>.toSuggestGenreDto() = SuggestedGenreRes(
    list = this.map { it.toResponseGenre() }
)

fun UpsertReq<GenreId>.toDtoUpsertGenre() = DtoUpsert<GenreId>(
    idList = this.list,
    operation = this.operation.toDtoUpsertOperation()
)

fun UpsertReq<ArtistId>.toDtoUpsertArtist() = DtoUpsert<ArtistId>(
    idList = this.list,
    operation = this.operation.toDtoUpsertOperation()
)

fun DtoPrevArtist.toPrevArtistRes() = PrevArtistRes(
    id = this.id,
    name = this.name,
    cover = this.cover
)

fun List<DtoPrevArtist>.toSuggestedArtistRes() = SuggestedArtistRes(
    list = this.map { it.toPrevArtistRes() }
)