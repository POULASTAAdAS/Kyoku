package com.poulastaa.domain.table

import org.jetbrains.exposed.dao.id.LongIdTable

object PlaylistTable : LongIdTable() {
    val name = varchar("name", 400)
    val points = long("points").default(0)
}