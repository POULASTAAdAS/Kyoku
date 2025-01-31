package com.poulastaa.kyoku.shardmanager.app.core.database.model

data class DtoGenreArtistRelation(
    val genreId: Int,
    val artistId: Long,
    val popularity: Long,
    val genre: String,
)