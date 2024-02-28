package com.poulastaa.domain.dao.user_artist

import com.poulastaa.data.model.db_table.user_artist.GoogleUserArtistRelationTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class GoogleUserArtistRelation(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<GoogleUserArtistRelation>(GoogleUserArtistRelationTable)

    var artistId by GoogleUserArtistRelationTable.artistId
    var userId by GoogleUserArtistRelationTable.userId
}