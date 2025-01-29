package com.poulastaa.core.database.dao

import com.poulastaa.core.database.entity.app.ShardEntityArtist
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ShardDaoArtist(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ShardDaoArtist>(ShardEntityArtist)

    var name by ShardEntityArtist.name
    var coverImage by ShardEntityArtist.coverImage
    var popularity by ShardEntityArtist.popularity
}