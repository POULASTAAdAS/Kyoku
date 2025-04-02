package com.poulastaa.kyoku.shardmanager.app.core.database.dao.shard.genre_artist

import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.genre_artist.ShardGenreArtistEntityArtist
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ShardDaoArtist(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ShardDaoArtist>(ShardGenreArtistEntityArtist)

    var name by ShardGenreArtistEntityArtist.name
    var coverImage by ShardGenreArtistEntityArtist.coverImage
    var popularity by ShardGenreArtistEntityArtist.popularity
}