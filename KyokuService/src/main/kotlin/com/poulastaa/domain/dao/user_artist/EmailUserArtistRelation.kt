package com.poulastaa.domain.dao.user_artist

import com.poulastaa.data.model.db_table.user_artist.EmailUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_genre.EmailUserGenreRelationTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class EmailUserArtistRelation(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<EmailUserArtistRelation>(EmailUserGenreRelationTable)

    var artistId by EmailUserArtistRelationTable.userId
    var userId by EmailUserArtistRelationTable.userId
}