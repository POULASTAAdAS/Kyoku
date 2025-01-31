package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object EntitySongInfo : Table(name = "SongInfo") {
    val songId = reference("songId", EntitySong.id, onDelete = ReferenceOption.CASCADE)
    val releaseYear = integer("releaseYear")
    val composer = varchar("composer", 200).nullable().default(null)
    val popularity = long("popularity").default(0)
}