package com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion

import org.jetbrains.exposed.dao.id.IdTable

object ShardEntityCountryPopularSong : IdTable<Long>(name = "CountryPopularSong") {
    override val id = long("songId").entityId()
    val countryId = integer("countryId").index()

    override val primaryKey = PrimaryKey(id, countryId)
}