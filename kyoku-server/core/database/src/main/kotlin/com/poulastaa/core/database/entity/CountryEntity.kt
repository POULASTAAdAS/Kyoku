package com.poulastaa.core.database.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object CountryEntity : IntIdTable(name = "Country") {
    val country = varchar("country", 40).uniqueIndex()
}
