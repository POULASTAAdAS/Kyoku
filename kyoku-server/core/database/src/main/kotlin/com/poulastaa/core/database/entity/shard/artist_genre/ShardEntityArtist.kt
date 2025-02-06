package com.poulastaa.core.database.entity.shard.artist_genre

import org.jetbrains.exposed.dao.id.IdTable

object ShardEntityArtist : IdTable<Long>(name = "Artist") {
    override val id = long("id").entityId()
    val name = varchar("name", 60).uniqueIndex()
    val coverImage = varchar("coverImage", 200).nullable().default(null)
    val popularity = long("popularity").default(0)

    override val primaryKey = PrimaryKey(id)
}



