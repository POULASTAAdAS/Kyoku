package com.poulastaa.domain.table.other

import org.jetbrains.exposed.dao.id.IntIdTable

object CountryTable : IntIdTable() {
    val name = varchar("name", 200).uniqueIndex()
}