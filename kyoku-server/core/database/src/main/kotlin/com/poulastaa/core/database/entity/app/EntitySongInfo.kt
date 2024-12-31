package com.poulastaa.core.database.entity.app

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object EntitySongInfo : LongIdTable(name = "SongInfo") {
    val songId = reference("songId", EntitySong.id, onDelete = ReferenceOption.CASCADE)
    val releaseYear = integer("releaseYear")
    val composer = varchar("composer", 200).nullable().default(null)
    val popularity = long("popularity").default(0)
}