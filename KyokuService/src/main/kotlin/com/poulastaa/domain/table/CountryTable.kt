package com.poulastaa.domain.table

import org.jetbrains.exposed.dao.id.IntIdTable

object CountryTable : IntIdTable() {
    val name = varchar("name", 200).uniqueIndex()
}