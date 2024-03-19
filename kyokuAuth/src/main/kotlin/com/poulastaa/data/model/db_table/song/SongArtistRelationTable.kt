package com.poulastaa.data.model.db_table.song

import com.poulastaa.data.model.db_table.ArtistTable
import org.jetbrains.exposed.dao.id.LongIdTable

object SongArtistRelationTable : LongIdTable() {
    val songId = long("songId").references(SongTable.id)
    val artistId = integer("artistId").references(ArtistTable.id)
}