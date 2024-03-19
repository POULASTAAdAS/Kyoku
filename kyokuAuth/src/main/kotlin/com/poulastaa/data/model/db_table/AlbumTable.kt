package com.poulastaa.data.model.db_table

import org.jetbrains.exposed.dao.id.LongIdTable

object AlbumTable : LongIdTable() {
    val name = varchar("name", 200).uniqueIndex()
    val points = long("points").default(0)
}