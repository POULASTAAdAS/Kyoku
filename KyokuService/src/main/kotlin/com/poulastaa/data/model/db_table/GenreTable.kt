package com.poulastaa.data.model.db_table

import org.jetbrains.exposed.dao.id.IntIdTable

object GenreTable : IntIdTable() {
    val genre = varchar("genre", 120).uniqueIndex()
    val points = long("points").default(0)
}