package com.poulastaa.domain.table.relation

import com.poulastaa.domain.table.PlaylistTable
import com.poulastaa.domain.table.SongTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object SongPlaylistRelationTable : Table() {
    val songId = long("songId").references(SongTable.id, onDelete = ReferenceOption.CASCADE)
    val playlistId = long("playlistId").references(PlaylistTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(songId, playlistId)
}