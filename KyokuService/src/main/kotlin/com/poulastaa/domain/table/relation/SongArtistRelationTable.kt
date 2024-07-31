package com.poulastaa.domain.table.relation

import com.poulastaa.domain.table.ArtistTable
import com.poulastaa.domain.table.SongTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object SongArtistRelationTable : Table() {
    val songId = long("songId").references(SongTable.id, onDelete = ReferenceOption.CASCADE)
    val artistId = long("artistId").references(ArtistTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(songId, artistId)
}