package com.poulastaa.domain.table

import org.jetbrains.exposed.dao.id.LongIdTable

object AlbumTable : LongIdTable() {
    val name = varchar("name", 100).uniqueIndex()
    val points = long("points").default(0)
}