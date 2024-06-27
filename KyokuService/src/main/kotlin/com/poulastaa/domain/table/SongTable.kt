package com.poulastaa.domain.table

import org.jetbrains.exposed.dao.id.LongIdTable

object SongTable : LongIdTable() {
    val title = text("title")
    val coverImage = text("coverImage")
    val masterPlaylistPath = text("masterPlaylistPath")
    val totalTime = varchar("totalTime", 50)
    val composer = text("composer").default("")
    val publisher = text("publisher").default("")
    val album_artist = text("album_artist").default("")
    val track = text("track").default("")
    val year = integer("year")
    val points = long("points").default(0)
}