package com.poulastaa.data.dao

import com.poulastaa.domain.table.GenreTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class GenreDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GenreDao>(GenreTable)

    val name by GenreTable.name
    var points by GenreTable.points
}