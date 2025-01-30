package com.poulastaa.core.database.entity.user

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntityUserGenre: Table(name = "UserSavedGenre") {
    val userId = long("userId").references(EntityUser.id, onDelete = ReferenceOption.CASCADE)
    val genreId = integer("genreId")

    override val primaryKey = PrimaryKey(userId, genreId)
}