package com.poulastaa.kyoku.shardmanager.app.core.database.dao.shard.genre_artist

import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.genre_artist.ShardEntityArtist
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ShardDaoArtist(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ShardDaoArtist>(ShardEntityArtist)

    var name by ShardEntityArtist.name
    var coverImage by ShardEntityArtist.coverImage
    var popularity by ShardEntityArtist.popularity
}