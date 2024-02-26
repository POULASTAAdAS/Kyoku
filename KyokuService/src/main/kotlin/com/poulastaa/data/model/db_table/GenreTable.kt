package com.poulastaa.data.model.db_table

import org.jetbrains.exposed.dao.id.IntIdTable

object GenreTable : IntIdTable() {
    val name = varchar("name", 120).uniqueIndex()
}