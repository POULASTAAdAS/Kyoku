package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku_user

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntityUserPlaylist : Table(name = "UserPlaylist") {
    val userId = long("userId").references(EntityUser.id, onDelete = ReferenceOption.CASCADE)
    val playlistId = long("playlistId")

    override val primaryKey = PrimaryKey(userId, playlistId)
}