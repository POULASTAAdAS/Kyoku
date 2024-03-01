package com.poulastaa.data.model.db_table

import org.jetbrains.exposed.sql.Table

object SongAlbumArtistRelationTable: Table() {
    val songId = long("songId").references(SongTable.id)
    val artistId = integer("artistId").references(ArtistTable.id)
    val albumId = long("albumId").references(AlbumTable.id)
}