package com.poulastaa.core.database.entity.shard.suggestion

import org.jetbrains.exposed.dao.id.IdTable

object ShardEntityYearPopularSong : IdTable<Long>(name = "YearPopularSong") {
    override val id = long("songId").entityId()
    val year = integer("year").index()

    override val primaryKey = PrimaryKey(id, year)
}