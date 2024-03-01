package com.poulastaa.domain.dao

import com.poulastaa.data.model.db_table.SongArtistRelationTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SongArtistRelation(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SongArtistRelation>(SongArtistRelationTable)

    val songId by SongArtistRelationTable.songId
    val artistId by SongArtistRelationTable.artistId
}