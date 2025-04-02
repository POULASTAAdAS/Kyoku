package com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.paging

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ShardPagingRelationEntityArtistSong : Table(name = "ArtistSong") {
    val songId = long("songId").references(ShardPagingEntitySong.id, onDelete = ReferenceOption.CASCADE)
    val artistId = long("artistId").references(ShardPagingEntityArtist.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(songId, artistId)
}
