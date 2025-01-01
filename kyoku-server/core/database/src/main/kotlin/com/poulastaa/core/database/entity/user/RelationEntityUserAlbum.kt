package com.poulastaa.core.database.entity.user

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntityUserAlbum : Table(name = "UserAlbum") {
    val userId = long("userId").references(EntityUser.id, onDelete = ReferenceOption.CASCADE)
    val albumId = long("albumId")

    override val primaryKey = PrimaryKey(userId, albumId)
}