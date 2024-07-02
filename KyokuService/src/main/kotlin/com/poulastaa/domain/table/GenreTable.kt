package com.poulastaa.domain.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.LongIdTable

object GenreTable : IntIdTable() {
    val name = varchar("name", 100).uniqueIndex()
    val points = long("points").default(0)
}