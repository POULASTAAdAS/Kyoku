package com.poulastaa.domain.dao.user_genre

import com.poulastaa.data.model.db_table.user_genre.PasskeyUserGenreRelationTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PasskeyUserGenreRelation(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<PasskeyUserGenreRelation>(PasskeyUserGenreRelationTable)

    var genreId by PasskeyUserGenreRelationTable.genreId
    var userID by PasskeyUserGenreRelationTable.userId
}