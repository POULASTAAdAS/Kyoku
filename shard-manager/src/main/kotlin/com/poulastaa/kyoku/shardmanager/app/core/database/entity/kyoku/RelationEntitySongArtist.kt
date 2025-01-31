package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntitySongArtist : Table(name = "SongArtist") {
    val songId = long("songId").references(EntitySong.id, onDelete = ReferenceOption.CASCADE)
    val artistId = long("artistId").references(EntityArtist.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(songId, artistId)
}