package com.poulastaa.domain.table.relation

import com.poulastaa.domain.table.PlaylistTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserPlaylistRelationTable : Table() {
    val playlistId = long("playlistId").references(PlaylistTable.id, onDelete = ReferenceOption.CASCADE)
    val userId = long("userId")
    val userType = varchar("userType", 20)

    override val primaryKey: PrimaryKey = PrimaryKey(playlistId, userId, userType)
}