package com.poulastaa.domain.dao.user_genre

import com.poulastaa.data.model.db_table.user_genre.GoogleUserGenreRelationTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class GoogleUserGenreRelation(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<GoogleUserGenreRelation>(GoogleUserGenreRelationTable)

    var genreId by GoogleUserGenreRelationTable.genreId
    var userId by GoogleUserGenreRelationTable.userId
}