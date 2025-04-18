package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku_user

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntityUserFavouriteSong : Table(name = "UserFavouriteSong") {
    val userId = long("userId").references(EntityUser.id, onDelete = ReferenceOption.CASCADE)
    val songId = long("songId")

    override val primaryKey = PrimaryKey(userId, songId)
}