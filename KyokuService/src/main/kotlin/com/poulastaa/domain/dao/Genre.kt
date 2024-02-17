package com.poulastaa.domain.dao

import com.poulastaa.data.model.db_table.GenreTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Genre(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Genre>(GenreTable)

    val genre by GenreTable.genre
    var points by GenreTable.points
}