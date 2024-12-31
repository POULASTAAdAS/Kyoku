package com.poulastaa.core.database.entity.user

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationUserGenre: Table(name = "UserGenre") {
    val userId = long("userId").references(EntityUser.id, onDelete = ReferenceOption.CASCADE)
    val genreId = long("genreId")

    override val primaryKey = PrimaryKey(userId, genreId)
}