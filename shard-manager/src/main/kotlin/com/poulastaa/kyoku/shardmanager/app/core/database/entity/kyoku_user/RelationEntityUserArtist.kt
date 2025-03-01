package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku_user

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntityUserArtist : Table(name = "UserSavedArtist") {
    val userId = long("userId").references(EntityUser.id, onDelete = ReferenceOption.CASCADE)
    val artistId = long("artistId")

    override val primaryKey = PrimaryKey(userId, artistId)
}