package com.poulastaa.core.database.entity.app

import org.jetbrains.exposed.dao.id.LongIdTable

object EntitySong : LongIdTable(name = "Song") {
    val title = varchar("title", 150).uniqueIndex()
    val poster = varchar("poster", 250).nullable().default(null)
    val masterPlaylist = varchar("masterPlaylist", 250)
}