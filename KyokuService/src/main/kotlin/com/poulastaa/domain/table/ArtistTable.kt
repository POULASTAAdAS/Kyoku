package com.poulastaa.domain.table

import org.jetbrains.exposed.dao.id.LongIdTable

object ArtistTable : LongIdTable() {
    val name = varchar("name", 400).uniqueIndex()
    val profilePicUrl = varchar("coverImage", 800).nullable()
    val points = long("points").default(0)
}