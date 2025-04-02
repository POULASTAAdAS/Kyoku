package com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.paging

import org.jetbrains.exposed.dao.id.LongIdTable

object ShardPagingEntityArtist : LongIdTable(name = "Artist") {
    val name = varchar("name", 60)
    val cover = varchar("cover", 200).nullable().default(null)
    val popularity = long("popularity").default(0)
}
