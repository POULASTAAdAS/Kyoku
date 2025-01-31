package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku

import org.jetbrains.exposed.dao.id.IntIdTable

object EntityCountry : IntIdTable(name = "Country") {
    val country = varchar("country", 40).uniqueIndex()
}