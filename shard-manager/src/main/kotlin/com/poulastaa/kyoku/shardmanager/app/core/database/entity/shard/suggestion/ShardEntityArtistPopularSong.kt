package com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion

import org.jetbrains.exposed.dao.id.IdTable

object ShardEntityArtistPopularSong : IdTable<Long>(name = "ArtistPopularSong") {
    override val id = long("songId").entityId()
    val artistId = long("artistId").index()
    val countryId = integer("countryId").index()

    override val primaryKey = PrimaryKey(id, artistId, countryId)
}