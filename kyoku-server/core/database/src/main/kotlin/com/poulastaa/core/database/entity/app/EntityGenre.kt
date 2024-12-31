package com.poulastaa.core.database.entity.app

import org.jetbrains.exposed.dao.id.IntIdTable

object EntityGenre : IntIdTable(name = "Genre") {
    val genre = varchar("genre", 30).uniqueIndex()
    val popularity = long("popularity").default(0)
}