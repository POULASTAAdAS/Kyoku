package com.poulastaa.domain.table.relation

import com.poulastaa.domain.table.ArtistTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ArtistAlbumRelationTable : Table() {
    val artistId = long("artistId").references(ArtistTable.id, onDelete = ReferenceOption.CASCADE)
    val albumId = long("albumId").references(ArtistTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(artistId, albumId)
}