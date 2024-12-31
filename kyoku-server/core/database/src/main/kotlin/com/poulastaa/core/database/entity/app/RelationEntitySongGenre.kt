package com.poulastaa.core.database.entity.app

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntitySongGenre : Table(name = "SongGenre") {
    val songId = long("songId").references(EntitySong.id, onDelete = ReferenceOption.CASCADE)
    val genreId = integer("genreId").references(EntityGenre.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(songId, genreId)
}