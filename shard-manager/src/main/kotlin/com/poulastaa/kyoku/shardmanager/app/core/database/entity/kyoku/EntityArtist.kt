package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku

import org.jetbrains.exposed.dao.id.LongIdTable

object EntityArtist : LongIdTable(name = "Artist") {
    val name = varchar("name", 60).uniqueIndex()
    val coverImage = varchar("coverImage", 200).nullable().default(null)
    val popularity = long("popularity").default(0)
}