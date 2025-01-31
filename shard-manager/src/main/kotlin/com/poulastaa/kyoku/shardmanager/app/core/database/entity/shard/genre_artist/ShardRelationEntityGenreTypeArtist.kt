package com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.genre_artist

import org.jetbrains.exposed.sql.Table

class ShardRelationEntityGenreTypeArtist(genre: String) : Table(name = "${genre}GenreIdArtistId") {
    val genreId = integer("genreId").index()
    val artistId = long("artistId").index()
    val popularity = long("popularity").default(0)

    override val primaryKey = PrimaryKey(genreId, artistId)
}