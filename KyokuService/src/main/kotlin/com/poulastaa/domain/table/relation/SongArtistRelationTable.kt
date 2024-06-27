package com.poulastaa.domain.table.relation

import com.poulastaa.domain.table.ArtistTable
import com.poulastaa.domain.table.SongTable
import org.jetbrains.exposed.sql.Table

object SongArtistRelationTable : Table() {
    val songId = long("songId").references(SongTable.id)
    val artistId = long("artistId").references(ArtistTable.id)

    override val primaryKey = PrimaryKey(songId, artistId)
}