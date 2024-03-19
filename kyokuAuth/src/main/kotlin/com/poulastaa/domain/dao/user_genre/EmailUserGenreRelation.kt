package com.poulastaa.domain.dao.user_genre

import com.poulastaa.data.model.db_table.user_genre.EmailUserGenreRelationTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class EmailUserGenreRelation(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<EmailUserGenreRelation>(EmailUserGenreRelationTable)

    var genreId by EmailUserGenreRelationTable.genreId
    var userId by EmailUserGenreRelationTable.userId
}