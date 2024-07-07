package com.poulastaa.domain.table.relation

import com.poulastaa.domain.table.AlbumTable
import com.poulastaa.domain.table.SongTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object SongAlbumRelationTable : Table() {
    val songId = long("songId").references(SongTable.id, onDelete = ReferenceOption.CASCADE)
    val albumId = long("albumId").references(AlbumTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(songId, albumId)
}