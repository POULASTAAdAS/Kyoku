package com.poulastaa.core.database.entity.shard.suggestion

import org.jetbrains.exposed.dao.id.IdTable

object ShardEntitySong : IdTable<Long>(name = "Song") {
    override val id = long("songId").entityId()
    val popularity = long("popularity").default(0).index()

    override val primaryKey = PrimaryKey(id)
}