package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku

import org.jetbrains.exposed.dao.id.LongIdTable

object EntityAlbum : LongIdTable(name = "Album") {
    val name = varchar("name", 100).uniqueIndex()
    val popularity = long("popularity").default(0)
}