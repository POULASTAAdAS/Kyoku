package com.poulastaa.core.database.entity.user

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntityUserArtist : Table(name = "UserArtist") {
    val userId = long("userId").references(EntityUser.id, onDelete = ReferenceOption.CASCADE)
    val artistId = long("artistId")

    override val primaryKey = PrimaryKey(userId, artistId)
}