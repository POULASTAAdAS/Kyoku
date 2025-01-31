package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku_user

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntityUserAlbum : Table(name = "UserSavedAlbum") {
    val userId = long("userId").references(EntityUser.id, onDelete = ReferenceOption.CASCADE)
    val albumId = long("albumId")

    override val primaryKey = PrimaryKey(userId, albumId)
}