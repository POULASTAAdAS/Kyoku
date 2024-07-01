package com.poulastaa.domain.table.relation

import com.poulastaa.domain.table.PlaylistTable
import com.poulastaa.domain.table.SongTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserPlaylistSongRelationTable : Table() {
    val playlistId = long("playlistId").references(PlaylistTable.id, onDelete = ReferenceOption.CASCADE)
    val userId = long("userId")
    val userType = varchar("userType", 20)
    val songId = long("songId").references(SongTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey: PrimaryKey = PrimaryKey(playlistId, userId, songId)
}