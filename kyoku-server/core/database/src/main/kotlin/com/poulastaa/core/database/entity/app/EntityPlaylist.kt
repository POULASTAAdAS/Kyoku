package com.poulastaa.core.database.entity.app

import org.jetbrains.exposed.dao.id.LongIdTable

object EntityPlaylist : LongIdTable(name = "Playlist") {
    val name = varchar("name", 100)
    val isPublic = bool("visibilityState").default(false)
    val popularity = long("popularity").default(0)
}