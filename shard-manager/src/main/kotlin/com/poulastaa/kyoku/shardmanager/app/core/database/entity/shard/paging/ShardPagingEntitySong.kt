package com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.paging

import org.jetbrains.exposed.dao.id.LongIdTable

object ShardPagingEntityArtist : LongIdTable(name = "Song") {
    val title = varchar("title", 150)
    val poster = varchar("poster", 200).nullable().default(null)
    val releaseYear = integer("releaseYear")
}
