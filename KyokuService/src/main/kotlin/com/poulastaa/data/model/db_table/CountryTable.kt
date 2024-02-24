package com.poulastaa.data.model.db_table

import org.jetbrains.exposed.dao.id.IntIdTable

object CountryTable : IntIdTable() {
    val name = varchar("name", 50).uniqueIndex()
}