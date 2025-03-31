package com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.paging

import org.jetbrains.exposed.dao.id.LongIdTable

object ShardPagingEntityAlbum : LongIdTable(name = "Album") {
    val title = varchar("title", 150)
    val poster = varchar("poster", 250).nullable().default(null)
    val releaseYear = integer("releaseYear")
    val popularity = long("popularity").default(0)
}
