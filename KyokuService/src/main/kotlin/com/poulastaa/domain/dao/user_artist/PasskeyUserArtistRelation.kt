package com.poulastaa.domain.dao.user_artist

import com.poulastaa.data.model.db_table.user_artist.PasskeyUserArtistRelationTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PasskeyUserArtistRelation(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<PasskeyUserArtistRelation>(PasskeyUserArtistRelationTable)

    var artistId by PasskeyUserArtistRelationTable.artistId
    var userId by PasskeyUserArtistRelationTable.userId
}