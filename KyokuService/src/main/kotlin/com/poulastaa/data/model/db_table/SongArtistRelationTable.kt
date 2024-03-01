package com.poulastaa.data.model.db_table

import org.jetbrains.exposed.dao.id.LongIdTable

object SongArtistRelationTable : LongIdTable() {
    val songId = long("songId").references(SongTable.id)
    val artistId = integer("artistId").references(ArtistTable.id)
}