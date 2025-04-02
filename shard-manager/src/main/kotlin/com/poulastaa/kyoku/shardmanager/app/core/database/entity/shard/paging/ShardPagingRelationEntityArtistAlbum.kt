package com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.paging

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ShardPagingRelationEntityArtistAlbum : Table(name = "ArtistAlbum") {
    val albumId = long("albumId").references(ShardPagingEntityAlbum.id, onDelete = ReferenceOption.CASCADE)
    val artistId = long("artistId").references(ShardPagingEntityArtist.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(albumId, artistId)
}
