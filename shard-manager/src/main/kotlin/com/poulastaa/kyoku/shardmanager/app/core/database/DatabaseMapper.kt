package com.poulastaa.kyoku.shardmanager.app.core.database

import com.poulastaa.kyoku.shardmanager.app.core.database.dao.kyoku.DaoAlbum
import com.poulastaa.kyoku.shardmanager.app.core.database.dao.kyoku.DaoArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.model.DtoAlbum
import com.poulastaa.kyoku.shardmanager.app.core.domain.model.DtoDBArtist

fun DaoArtist.toDbArtistDto() = DtoDBArtist(
    id = this.id.value,
    name = this.name,
    coverImage = this.coverImage,
    popularity = this.popularity
)


fun DaoAlbum.toDtoAlbum(releaseYear: Int, poster: String?) = DtoAlbum(
    id = this.id.value,
    name = this.name,
    poster = poster,
    popularity = this.popularity,
    releaseYear = releaseYear
)
