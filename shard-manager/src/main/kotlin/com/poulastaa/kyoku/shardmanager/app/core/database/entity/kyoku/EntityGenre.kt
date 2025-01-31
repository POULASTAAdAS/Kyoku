package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku

import org.jetbrains.exposed.dao.id.IntIdTable

object EntityGenre : IntIdTable(name = "Genre") {
    val genre = varchar("name", 30).uniqueIndex()
    val cover = varchar("cover", 200).nullable().default(null)
    val popularity = long("popularity").default(0)
}