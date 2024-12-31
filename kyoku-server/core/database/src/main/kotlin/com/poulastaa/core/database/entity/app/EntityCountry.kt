package com.poulastaa.core.database.entity.app

import org.jetbrains.exposed.dao.id.IntIdTable

object EntityCountry : IntIdTable(name = "Country") {
    val country = varchar("country", 40).uniqueIndex()
}