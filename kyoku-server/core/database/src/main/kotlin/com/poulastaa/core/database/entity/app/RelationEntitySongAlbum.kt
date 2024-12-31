package com.poulastaa.core.database.entity.app

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntitySongAlbum : Table(name = "SongAlbum") {
    val songId = long("songId").references(EntitySong.id, onDelete = ReferenceOption.CASCADE)
    val albumId = long("albumId").references(EntityAlbum.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(songId, albumId)
}